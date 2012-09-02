// Common plans


/* Initial beliefs and rules */

buy_battery.

// conditions for goal selection
is_energy_goal 					:- energy(MyE) & maxEnergy(Max) & MyE < Max/3.
is_recharge_goal 				:- energy(MyE) & maxEnergy(Max) & MyE < Max.
is_buy_goal    					:- money(M) & M >= 2 & not stop_buy.
is_move_goal	 					:- target(X) & not jia.is_at_target(X).
is_on_target_goal	 			:- target(X) & jia.is_at_target(X).
is_call_help_goal 			:- health(0) & not need_help.
is_disabled_goal				:- health(0) & need_help.
is_not_need_help_goal		:- health(X) & maxHealth(X) & need_help.
is_escape_goal					:- position(X) & jia.has_saboteur_at(X) & not health(0).
is_parry_goal 					:- position(X) & jia.has_saboteur_at(X) & not health(0) & not jia.has_coworker_at(X).
is_survey_goal 					:- jia.is_survey_goal.  // some edge to adjacent vertex is not surveyed
is_move_to_zone_goal		:- position(X) & not jia.is_on_team_zone.

/* General plans */

// the following plan is used to send only one action each cycle
+!do_and_wait_next_step(Act)
	: step(S)
	<-	.print("(step: ",S,") Executing action ",Act);
			Act; // perform the action (i.e., send the action to the simulator)
     	!wait_next_step(S). // wait for the next step before going on

+!wait_next_step(S)  : step(X) & X > S.
+!wait_next_step(S) <- .wait( { +step(_) }, 200, _); !wait_next_step(S).

 
+!init_goal(G)
	:	step(S) & position(V) & energy(E) & maxEnergy(Max)
	<-	//.print("I am at ",V," (",E,"/",Max,"), the goal for step ",S," is ",G);
			.print("(step: ",S,") Executing goal ",G);
      !G.

+!init_goal(G)
	<-	.print("No step yet... wait a bit");
      .wait(300);
	  	!init_goal(G).


//+step(S) <- .print("Current step is ", S).	// used for debug purposes
+step(S)
	<-	.send(coordinator,tell,step(S)).

/* Common Action Plans */

/* go to plan */
+!go_to("none")
	<-	!do_and_wait_next_step(skip).
+!go_to(Target)
	<-	!do_and_wait_next_step(goto(Target)).


/* plans for energy */
+!be_at_full_charge 
    : energy(MyE)
   <- //.print("My energy is ",MyE,", recharging");
      !do_and_wait_next_step(recharge). // otherwise, recharge


/* call help */
+!call_help
	<-	jia.get_repairers(Agents);
			!send_help(Agents);
			+need_help;
			!send_status(disabled);
			!alert_saboteur.

+!send_help([X|TAg])
	: .my_name(Me)
	<-	.print("sending need_help to ",X);
 	   	.send(X,tell,need_help(Me));
 	   	!send_help(TAg).
+!send_help([]).

+!alert_saboteur
	: position(X) & jia.has_unique_opponent_at(X,Ag)
	<-	.broadcast(tell,saboteur(Ag,X)).
+!alert_saboteur.


/* not need help */
+!not_need_help
	<-	jia.get_repairers(Agents);
			!send_not_need_help(Agents);
			-need_help;
			!send_status(normal).

+!send_not_need_help([X|TAg])
	: .my_name(Me)
	<-	.print("sending not_need_help to ",X);
 	   	.send(X,tell,not_need_help(Me));
 	   	!send_not_need_help(TAg).
+!send_not_need_help([]).


/* send status */
+!send_status(S)
	: .my_name(Me)
	<-	.broadcast(tell,coworkerStatus(Me,S));
			jia.set_my_status(S).


/* go to repairer */
+!go_to_repairer
	<-	jia.closer_repairer(Pos);
			!move_to_repairer(Pos).

+!move_to_repairer(X)
	: jia.is_at_target(X)
	<-	!call_help;
			!do_and_wait_next_step(recharge).

+!move_to_repairer(X)
	<-	!move_to(X).


/* parry plan */
+!parry
	<-	!do_and_wait_next_step(parry).


/* buy battery */
+!buy(X)
    : money(M)
   <- //.print("I am going to buy ",X,"! My money is ",M);
      !do_and_wait_next_step(buy(X)).


/* random walk plans */
+!random_walk 
    : position(MyV) // my location
   <- //jia.random_walk(MyV,Target);
   		jia.least_visited_neighbor(MyV,Target);
   		!go_to(Target).
-!random_walk[error(I),error_msg(M)]
	<-	.print("failure in random_walk! ",I,": ",M).


/* escape plan */
+!escape
	<-	jia.escape(Target);
		  !go_to(Target).


/* move to taget plans */
+!move_to_target
	:	target(Y) & position(X)
	<-	jia.move_to_target(X,Y,NextPos);
			!go_to(NextPos).
-!move_to_target[error(I),error_msg(M)]
	<-	.print("failure in move_to_target! ",I,": ",M).


/* move to plans */
+!move_to("none")
	<-	!init_goal(random_walk).

+!move_to(Pos)
	: position(X)
	<-	jia.move_to_target(X,Pos,NextPos);
			!go_to(NextPos).


/* move to zone plan */
+!move_to_zone
	<-	jia.closer_coworker(Pos);
			!move_to(Pos).


/* survey plans */
+!survey
   <- !do_and_wait_next_step(survey).


/* wait plan */
+!wait
	<-	!do_and_wait_next_step(skip).

