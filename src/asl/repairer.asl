// Agent Repairer

/* Initial beliefs and rules */
is_not_help_goal					:-	not_need_help(Ag).
is_repair_goal						:-	need_help(Ag) & jia.agent_position(Ag,Pos) & position(Pos) & not jia.has_another_repairer.
is_wait_to_repair_goal		:- 	need_help(Ag) & jia.agent_position(Ag,Pos) & jia.is_neighbor_vertex(Pos) & not jia.has_another_repairer & not jia.has_repairer_at(Pos).

/* Initial goals */

+!repair_zone1_goal
	<-	//.print("Starting repair_zone1 goal"); 
			!select_repairer_goal.

+!repair_zone2_goal
	<-	//.print("Starting repair_zone2 goal"); 
			!select_repairer_goal.


+!select_repairer_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_repairer_goal.

+!select_repairer_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_not_help_goal
	<-	!init_goal(not_help);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_escape_goal
	<-	!init_goal(escape);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_repair_goal
	<-	!init_goal(repair);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_wait_to_repair_goal
	<-	!init_goal(wait);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_repairer_goal.

+!select_repairer_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_buy_goal
	<-	!init_goal(repairer_buy);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_repairer_goal.

+!select_repairer_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_repairer_goal.

+!select_repairer_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_repairer_goal.

+!select_repairer_goal
	<- 	!init_goal(random_walk);
			!!select_repairer_goal.


/* Repair plans */

+!repair
	: need_help(Ag) & jia.agent_position(Ag,Pos) & position(Pos)
	<-	jia.agent_server_id(Ag,Id);
			!do_and_wait_next_step(repair(Id)).
+!repair
	<-	!init_goal(wait).

+!not_help
	: need_help(Ag) & not_need_help(Ag)
	<-	.abolish(need_help(Ag));
			.abolish(not_need_help(Ag)).

+!not_help
	: not_need_help(Ag)
	<-	.abolish(not_need_help(Ag)).


/* Buy plans */
+!repairer_buy
	: maxEnergy(E) & E >= 35 & maxHealth(X) & X >= 5
	<- +stop_buy.

+!repairer_buy
	: buy_battery & maxEnergy(E) & E < 35
	<-	!buy(battery);
			-buy_battery;
			+buy_shield.

+!repairer_buy
	: buy_battery
	<-	-buy_battery;
			+buy_shield.

+!repairer_buy
	: buy_shield & maxHealth(X) & X < 5
	<-	!buy(shield);
			-buy_shield;
			+buy_battery.

+!repairer_buy
	: buy_shield
	<-	-buy_shield;
			+buy_battery.