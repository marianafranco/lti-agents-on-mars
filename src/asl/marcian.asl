// Agent Marcian

// include role plans for agents
{ include("common.asl") }
{ include("explorer.asl") }
{ include("inspector.asl") }
{ include("repairer.asl") }
{ include("saboteur.asl") }
{ include("sentinel.asl") }

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

// join the created workspace and lookup the org artifacts
+!start 
	<- 	!join;
			lookupArtifact("marsGroupBoard",GrId);
			focus(GrId).

-!start
	<- 	.wait(200);
			!!start.

+!join 
	<- 	.my_name(Me);
			joinWorkspace("marsWS",_).
-!join
	<- 	.wait(200);
			!!join.


// keep focused on schemes my groups are responsible for
+schemes(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.

// keep focused on subgroups
+subgroups(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.


// init
+simStart
	<-	!!init.

+!init
	:	availableRole(R,M,S,G) & environment(ok)
	<-	!!restart.

+!init
	: schemes(L) & environment(ok)
	<-	!!playRole.

+!init
	<-	.wait({+environment(ok)},200,_);
			!!init.

+!restart
	:	availableRole(R,M,S,G) & role(X) & simStart
	<-	-role(X);
		  +role(R);
			!!commit_to_mission.

-!restart
	<- 	.wait(200);
			!!restart.


// plan to start to play a role
+!playRole
	:	role(R) & simStart
	<- 	jia.to_lower_case(R,S);
		  -role(R);
		  +role(S);
			.print("I want to play role ",S);
			.send(coordinator,tell,want_to_play(S)).

-!playRole
	<- 	.wait(200);
			!!playRole.

+availableRole(R,M,S,G)
	<-	lookupArtifact(G,GrId);
			adoptRole(R)[artifact_id(GrId)];
//			.print("I'm playing ",R, " on ",G);
			!check_play(Ag,R);
			!!commit_to_mission.

+!check_play(A,R)
	:	play(A,R,_) & .my_name(A).

+!check_play(A,R)
	<- 	.wait({+play(_,_,_)},200,_);
			adoptRole(R)[artifact_id(GrArtId)];
			!check_play(A,R,_).


// plans to commit to missions which the agent has permission
+!commit_to_mission
	:	availableRole(R,mExplore,S,G)
	<-	//.print("I will try to commit to mExplore");
			commitMission(mExplore)[artifact_name("zone1Sch")];
			!!check_commit_mission(mExplore,"zone1Sch").

+!commit_to_mission
	:	availableRole(explorer,mOccupyZone1,S,G)
	<-	//.print("I will try to commit to mOccupyZone1");
			commitMission(mOccupyZone1)[artifact_name("zone1Sch")];
			!!check_commit_mission(mOccupyZone1,"zone1Sch").

+!commit_to_mission
	:	availableRole(R,mInspect,S,G)
	<-	//.print("I will try to commit to mInspect");
			commitMission(mInspect)[artifact_name("zone1Sch")];
			!!check_commit_mission(mInspect,"zone1Sch").

+!commit_to_mission
	:	availableRole(R,mInspect,S,G)
	<-	//.print("I will try to commit to mInspect");
			commitMission(mInspect)[artifact_name("zone1Sch")];
			!!check_commit_mission(mInspect,"zone1Sch").

+!commit_to_mission
	:	availableRole(inspector,mOccupyZone1,S,G)
	<-	//.print("I will try to commit to mOccupyZone1");
			commitMission(mOccupyZone1)[artifact_name("zone1Sch")];
			!!check_commit_mission(mOccupyZone1,"zone1Sch").

+!commit_to_mission.	// the agent will commit to the mission through the obligations handler 

-!commit_to_mission
	: availableRole(R,M,S,G)
	<-	.print("[ERROR] I could not commit to ", M).


// plans to handle obligations
+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
	: .my_name(Ag)
   <- //.print("I am obliged to commit to ",Mission," on ",Scheme);
      commitMission(Mission)[artifact_name(Scheme)];
      !!check_commit_mission(Mission,Scheme).

// could not use this to not lose steps in the beggining of the simulation
//+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),DeadLine)
//    : .my_name(Ag)
//   <- .print("I am obliged to achieve goal ",Goal);
//      !Goal[scheme(Scheme)].
//      //goalAchieved(Goal)[artifact_name(Scheme)].


// check commitment to mission
+!check_commit_mission(M,S)
	:	.my_name(A) & commitment(A,M,_) & play(A,R,G)
	<-	.print("I commited to ", M);
			.broadcast(tell,coworker(A,R,M,G));		// broadcast
			!start_goal(M).

+!check_commit_mission(M,S)
	<-	.wait({+commitment(_,_,_)},500,_);
			.print("[ERROR] Trying again to commit to ",M," on ",S);
			commitMission(M)[artifact_name(S)];
			!!check_commit_mission(M,S).


+!start_goal(mExplore)
	<-	!!explore_goal.

+!start_goal(mOccupyZone1)
	<-	!!occupy_zone1_goal.

+!start_goal(mRepairZone1)
	<-	!!repair_zone1_goal.

+!start_goal(mInspect)
	<-	!!inspect_goal.

+!start_goal(mOccupyZone2)
	<-	!!occupy_zone2_goal.

+!start_goal(mRepairZone2)
	<-	!!repair_zone2_goal.

+!start_goal(mSabotage)
	<-	!!sabotage_goal.

+!start_goal(mSentinelSabotage)
	<-	!!help_sabotage_goal.


-!occupy_zone1_goal
	<-	.wait({+role(_)},200,_);
			!!occupy_zone1_goal.

-!occupy_zone2_goal
	<-	.wait({+role(_)},200,_);
			!!occupy_zone2_goal.


// start new mission
+!start_new_mission(M,S)
	: obligation(Me,Norm,achieved(Scheme,Goal,Ag),DeadLine) & .my_name(Me)
	<-	.print("Achived goal ", Goal);
			goalAchieved(Goal)[artifact_name(Scheme)];
			//.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(S)];
			!check_commit_mission(M,S).
+!start_new_mission(M,S)
	<-	//.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(S)];
			!check_commit_mission(M,S).

/* Plans to finish the simulation and start a new one */

+simEnd
   <- .drop_all_desires;
   		//.drop_all_intentions;
   		!remove_percepts;
   		.send(coordinator,tell,simEnd);
			-simEnd;
			-simStart;
			jia.restart_world_model.

+!remove_percepts
	<-	.abolish(environment(_));
			.abolish(achievement(_));
			.abolish(coworker(_,_,_));
			.abolish(coworkerPosition(_,_));
			.abolish(target(_));
			//.abolish(availableRole(_,_,_,_)).
			.abolish(role(_)).
