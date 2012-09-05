// Agent Saboteur

/* Initial beliefs and rules */

// conditions for goal selection
is_attack_goal 					:- 	jia.has_opponent_on_vertex.

/* Initial goals */

/******************** Sabotage goal ***********************/
+!sabotage_goal
	:	role(saboteur)
	<- 	//.print("Starting sabotage goal");
			!select_sabotage_goal.

+!sabotage_goal
	<-	.wait({+role(saboteur)},200,_);
			!!sabotage_goal.


+!select_sabotage_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_sabotage_goal.

+!select_sabotage_goal
	: is_attack_goal
	<-	!init_goal(attack);
			!!select_sabotage_goal.

+!select_sabotage_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_buy_goal
	<-	!init_goal(saboteur_buy);
			!!select_sabotage_goal.

+!select_sabotage_goal
	<- 	!init_goal(go_attack);
			!!select_sabotage_goal.




/******************** Occupy zone goal ***********************/
+!occupy_zone1_goal
	:	role(saboteur)
	<-	//.print("Starting occupy_zone1 goal");
			!select_saboteur_goal.

+!occupy_zone2_goal
	:	role(saboteur)
	<-	//.print("Starting occupy_zone2 goal");
			!select_saboteur_goal.


+!select_saboteur_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_saboteur_goal.

+!select_saboteur_goal
	: is_attack_goal
	<-	!init_goal(attack);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_saboteur_goal.

+!select_saboteur_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_buy_goal
	<-	!init_goal(saboteur_buy);
			!!select_saboteur_goal.

+!select_saboteur_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_saboteur_goal.

+!select_saboteur_goal
	<- 	!init_goal(random_walk);
			!!select_saboteur_goal.



/**************** Plans *****************/

/* Plans for attack */

+!go_attack
	<-	jia.select_opponent_to_attack(Pos);
			!move_to(Pos).

+!attack
	<-	jia.get_opponent_name(Enemy);
			!do_and_wait_next_step(attack(Enemy)).


/* Buy plans */
+!saboteur_buy
	: buy_battery
	<-	!buy(battery);
			-buy_battery;
			+buy_sabotage.

+!saboteur_buy
	: buy_sabotage
	<-	!buy(sabotageDevice);
			-buy_sabotage;
			+buy_shield.

+!saboteur_buy
	: buy_shield
	<-	!buy(shield);
			-buy_shield;
			+buy_battery.