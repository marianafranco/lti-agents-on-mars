// Agent Inspector

/* Initial beliefs and rules */

// conditions for goal selection
is_inspect_goal	:- jia.is_inspect_goal.


/* Initial goals */

+!inspector_goal
	<-	.print("Starting inspector_goal");
			!select_inspector_goal.


+!select_inspector_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_inspector_goal.

+!select_inspector_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_inspector_goal.

+!select_inspector_goal
	: is_parry_goal
	<- 	!init_goal(random_walk);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_inspector_goal.

+!select_inspector_goal
	: is_inspect_goal
	<- 	!init_goal(inspect);
			!!select_inspector_goal.

+!select_inspector_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_buy_goal
	<-	!init_goal(inspector_buy);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_wait_goal
	<-	!init_goal(wait);
			!!select_inspector_goal.

+!select_inspector_goal
	<- 	!init_goal(random_walk);
			!!select_inspector_goal.


/* Inspect plans */
+!inspect
	<-	!do_and_wait_next_step(inspect).

/* Buy plans */
+!inspector_buy
	: buy_battery
	<-	!buy(battery);
			-buy_battery;
			+buy_shield.

+!inspector_buy
	: buy_shield
	<-	!buy(shield);
			-buy_shield;
			+buy_battery.