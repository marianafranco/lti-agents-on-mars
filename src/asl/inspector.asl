// Agent Inspector

/* Initial beliefs and rules */

// conditions for goal selection
is_inspect_goal						:- jia.is_inspect_goal & not lastAction(inspect).
is_inspect_role_goal			:- jia.is_inspect_role_goal.
has_uninspected_opponent	:- jia.has_uninspected_opponent.

/* Initial goals */

/******************* Inspect goal ******************/
+!inspect_goal
	<-	.print("Starting inspect goal");
			!select_inspect_goal.


+!select_inspect_goal
	: not has_uninspected_opponent
	<-	!init_goal(start_new_mission(mOccupyZone));
			!occupy_zone_goal.

+!select_inspect_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_inspect_goal.

+!select_inspect_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_inspect_goal.

+!select_inspect_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_inspect_goal.

+!select_inspect_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_parry_goal
	<- 	!init_goal(escape);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_inspect_role_goal
	<- 	!init_goal(inspect);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_inspect_goal.

+!select_inspect_goal
	:	is_buy_goal
	<-	!init_goal(inspector_buy);
			!!select_inspect_goal.

+!select_inspect_goal
	: has_uninspected_opponent
	<-	!init_goal(go_to_uninspected);
			!!select_inspect_goal.

+!select_inspect_goal
	<- 	!init_goal(random_walk);
			!!select_inspect_goal.



/****************** Occupy zone goal *********************/
+!occupy_zone_goal
	:	role(inspector)
	<-	.print("Starting occupy_zone goal");
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
	<- 	!init_goal(escape);
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
	:	is_can_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_inspector_goal.

+!select_inspector_goal
	<- 	!init_goal(random_walk);
			!!select_inspector_goal.


/****************** Plans ***************************/

/* Go to uninspected opponent */
+!go_to_uninspected
	:	position(X)
	<-	jia.closer_uninspected_opponent(Pos);
			jia.move_to_target(X,Pos,NextPos);
			!do_and_wait_next_step(goto(NextPos)).

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