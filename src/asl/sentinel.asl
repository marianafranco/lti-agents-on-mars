// Agent Sentinel

/* Initial beliefs and rules */


// conditions for goal selection

/* Initial goals */

/*************** Help sabotage goal *******************/

+!help_sabotage_goal
	:	role(sentinel)
	<- 	//.print("Starting sabotage goal");
			!select_help_sabotage_goal.

+!help_sabotage_goal
	<-	.wait({+role(sentinel)},200,_);
			!!help_sabotage_goal.


+!select_help_sabotage_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	:	is_buy_goal
	<-	!init_goal(sentinel_buy);
			!!select_help_sabotage_goal.

+!select_help_sabotage_goal
	<- 	!init_goal(help_sabotage);
			!!select_help_sabotage_goal.



/*************** Occupy zone goal *****************/

+!occupy_zone1_goal
	:	role(sentinel)
	<-	//.print("Starting occupy_zone1 goal");
			!select_sentinel_goal.

+!occupy_zone2_goal
	:	role(sentinel)
	<-	//.print("Starting occupy_zone2 goal");
			!select_sentinel_goal.


+!select_sentinel_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_sentinel_goal.
	
+!select_sentinel_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_sentinel_goal.

+!select_sentinel_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_buy_goal
	<-	!init_goal(sentinel_buy);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_goal.

+!select_sentinel_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_sentinel_goal.

+!select_sentinel_goal
	<- 	!init_goal(random_walk);
			!!select_sentinel_goal.


/**************** Plans *****************/


/* Help sabotage plan */
+!help_sabotage
	<-	jia.select_opponent_vertex(Pos);
			!move_to(Pos).


/* Buy plans */
+!sentinel_buy
	: maxEnergy(E) & E >= 30 & maxHealth(X) & X >= 4
	<- +stop_buy.

+!sentinel_buy
	: buy_battery & maxEnergy(E) & E < 30
	<-	!buy(battery);
			-buy_battery;
			+buy_shield.

+!sentinel_buy
	: buy_battery
	<-	-buy_battery;
			+buy_shield.

+!sentinel_buy
	: buy_shield & maxHealth(X) & X < 4
	<-	!buy(shield);
			-buy_shield;
			+buy_battery.

+!sentinel_buy
	: buy_shield
	<-	-buy_shield;
			+buy_battery.

+!sentinel_buy
	<-	+buy_battery.