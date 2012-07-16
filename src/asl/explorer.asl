// Agent Explorer

/* Initial beliefs and rules */

// conditions for goal selection
is_probe_goal  :- position(MyV) & not jia.is_probed_vertex(MyV) & role(explorer).

/* Initial goals */

+!explorer_goal
	<-	.print("Starting explorer_goal");
			!select_explorer_goal.


+!select_explorer_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_explorer_goal.

+!select_explorer_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_explorer_goal.

+!select_explorer_goal
	: is_parry_goal
	<-	!init_goal(random_walk);
			!!select_explorer_goal.

+!select_explorer_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_explorer_goal.

+!select_explorer_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_buy_goal
	<-	!init_goal(explorer_buy);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_wait_goal
	<-	-target(_);
			!init_goal(move_to_not_probed);
			!!select_explorer_goal.

+!select_explorer_goal
	<- 	!init_goal(agents_coordination);
			!!select_explorer_goal.

-!select_explorer_goal[error(I),error_msg(M)]
	<-	.print("failure in select_explorer_goal! ",I,": ",M).



/* Probe plans */

+!probe
   <- .print("Probing my location");
      !do_and_wait_next_step(probe).


/* Agents coordination plans */

+!agents_coordination
	: step(S)
	<- 	jia.agents_coordination(A,P);
			.print("New formation!! ", .length(P));
			!send_target(A,P);
			!wait_next_step(S).

+!send_target([X|TAg],[Y|TLoc])
 	<- 	.print("send: ",X, ", " ,Y);
 	   	.send(X,tell,target(Y));
 	   	!send_target(TAg,TLoc).
+!send_target([],[]).

-!send_target[error(I),error_msg(M)]
	<-	.print("failure in send_target! ",I,": ",M).


/* Move to not probed */

+!move_to_not_probed
	: position(MyV) // my location
	<- jia.move_to_not_probed(MyV,Target);
		 !do_and_wait_next_step(goto(Target)).

/* Buy plans */
+!explorer_buy
	: buy_battery
	<-	!buy(battery);
			-buy_battery;
			+buy_shield.

+!explorer_buy
	: buy_shield
	<-	!buy(shield);
			-buy_shield;
			+buy_battery.