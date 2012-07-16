// Agent Saboteur

/* Initial beliefs and rules */

// conditions for goal selection
is_attack_goal 					:- 	jia.has_opponent_on_vertex.
is_attack_saboteur_goal	:-	jia.found_active_saboteur.
is_attack_repairer_goal	:-	jia.found_active_repairer.

/* Initial goals */

+!saboteur_goal
	<- 	.print("Starting saboteur_goal");
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
	:	is_attack_saboteur_goal
	<-	!init_goal(attack_saboteur);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_attack_repairer_goal
	<-	!init_goal(attack_repairer);
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
	:	is_wait_goal
	<-	!init_goal(wait);
			!!select_saboteur_goal.

+!select_saboteur_goal
	<- 	!init_goal(random_walk);
			!!select_saboteur_goal.


/* Plans for attack */

+!attack
	<-	jia.get_opponent_name(Enemy);
			.print("Attacked ", Enemy);
			!do_and_wait_next_step(attack(Enemy)).

+!attack_saboteur
	:	position(X)
	<-	jia.closer_opponent("saboteur",Pos);
			jia.move_to_target(X,Pos,NextPos);
			!do_and_wait_next_step(goto(NextPos)).

+!attack_repairer
	:	position(X)
	<-	jia.closer_opponent("repairer",Pos);
			jia.move_to_target(X,Pos,NextPos);
			!do_and_wait_next_step(goto(NextPos)).


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