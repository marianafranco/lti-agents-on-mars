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
//			!playRole.
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

+subgroups(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.

+simStart
	: schemes([])
	<-	.wait({+schemes(_)},200,_);
			!!playRole.

+simStart
	: schemes(L)
	<-	!!playRole.

+simStart
	<-	.wait({+schemes(_)},200,_);
			!!playRole.

// plan to start to play a role
+!playRole
	:	role(R) & .my_name(Ag) & simStart
	<- 	jia.to_lower_case(R,S);
		  -role(R);
		  +role(S);
		  .print("I'll play role ",S);
			//adoptRole(S)[artifact_id(GrArtId)];
			!adopt_role(S);
			!check_play(Ag,S,_);
			!commit_to_mission.

-!playRole
	<- 	.wait(200);
			!!playRole.

+!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"sabotageGroupBoard")]
		& role(saboteur) & not play(_,saboteur,"sabotageGroupBoard")
	<-	!adopt_role_sabotage_group(S,GrArtId).
+!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"sabotageGroupBoard")]
		& role(sentinel) & not play(_,sentinel,"sabotageGroupBoard")
	<-	!adopt_role_sabotage_group(S,GrArtId).

+!adopt_role_sabotage_group(S,GrArtId)
	<-	adoptRole(S)[artifact_id(GrArtId)].
-!adopt_role_sabotage_group(S,GrArtId)
	<-	.print("[1] Trying again to adopt role ", S);
			!adopt_role(S).	// try again

+!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"zone1GroupBoard")]
		& not play(_,S,"zone1GroupBoard")
	<-	adoptRole(S)[artifact_id(GrArtId)].
-!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"zone1GroupBoard")]
		& not play(_,S,"zone1GroupBoard")
	<-	.print("[2] Trying again to adopt role ", S);
			!adopt_role(S).	// try again

+!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"zone2GroupBoard")]
		& not play(_,S,"zone2GroupBoard")
	<-	.print("Trying to adopt role ", S, " on zone2GroupBoard");
			adoptRole(S)[artifact_id(GrArtId)].
-!adopt_role(S)
	:	formationStatus(nok)[artifact_name(GrArtId,"zone2GroupBoard")]
		& not play(_,S,"zone2GroupBoard")
	<-	.print("[3] Trying again to adopt role ", S);
			!adopt_role(S).	// try again

+!adopt_role(S)
	<-	lookupArtifact("zone1GroupBoard",GrId);
			adoptRole(S)[artifact_id(GrId)].
-!adopt_role(S)
	<-	.print("[4] Trying again to adopt role ", S);
			!adopt_role_last_try(S).

+!adopt_role_last_try(S)
	<-	lookupArtifact("zone2GroupBoard",GrId);
			adoptRole(S)[artifact_id(GrId)].
-!adopt_role_last_try(S)
	<-	.print("[5] Trying again to adopt role ", S);
			lookupArtifact("zone1GroupBoard",GrId2);
			adoptRole(S)[artifact_id(GrId2)].

+!check_play(A,R,_)
	:	play(A,R,_) & .my_name(A).

+!check_play(A,R,_)
	<- 	.wait({+play(_,_,_)},200,_);
			adoptRole(R)[artifact_id(GrArtId)];
			!check_play(A,R,_).


// plans to commit to missions

// explorer
//+!commit_to_mission
//	:	role(explorer).		// explorers will commit to missions through the obligations handler
+!commit_to_mission
	:	goalState("zone1Sch",_,_,_,_) & .my_name(Ag) & play(Ag,explorer,"zone1GroupBoard")
	<-	.print("I will try to commit to mExplore");
			commitMission(mExplore)[artifact_name("zone1Sch")];
			!check_commit_mission(mExplore,"zone1Sch").
-!commit_to_mission
	: role(explorer)
	<-	.print("I could not commit to mExplorer");
			.print("I will try to commit to mOccupyZone1");
			commitMission(mOccupyZone1)[artifact_name("zone1Sch")];
			!check_commit_mission(mOccupyZone1,"zone1Sch").
+!commit_to_mission
	:	role(explorer).


// repairer
+!commit_to_mission
	:	role(repairer).		// repairers will commit to missions through the obligations handler

// saboteur
+!commit_to_mission
	:	role(saboteur).

// sentinel
+!commit_to_mission
	: role(sentinel).

// inspector
+!commit_to_mission
	:	goalState("zone1Sch",_,_,_,_)  & .my_name(Ag) & play(Ag,inspector,"zone1GroupBoard")
	<-	.print("I will try to commit to mInspect");
			commitMission(mInspect)[artifact_name("zone1Sch")];
			!check_commit_mission(mInspect,"zone1Sch").
-!commit_to_mission
	: role(inspector)
	<-	.print("I could not commit to mInspect");
			.print("I will try to commit to mOccupyZone1");
			commitMission(mOccupyZone1)[artifact_name("zone1Sch")];
			!check_commit_mission(mOccupyZone1,"zone1Sch").
+!commit_to_mission
	:	role(inspector).


+!commit_to_mission
	<-	.wait({+goalState("sch1",_,_,_,_)},200,_);
			!commit_to_mission.


// plans to handle obligations
+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
	: .my_name(Ag)
   <- .print("I am obliged to commit to ",Mission," on ",Scheme);
      commitMission(Mission)[artifact_name(Scheme)];
      !check_commit_mission(Mission,Scheme).

+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),DeadLine)
    : .my_name(Ag)
   <- .print("I am obliged to achieve goal ",Goal);
      !Goal[scheme(Scheme)];
      goalAchieved(Goal)[artifact_name(Scheme)].


// check commitment to mission
+!check_commit_mission(M,S)
	:	.my_name(A) & commitment(A,M,_) & role(R)
	<-	.print("I commited to ", M);
			.broadcast(tell,coworker(A,R,M)).			// broadcast

+!check_commit_mission(M,S)
	<-	.wait({+commitment(_,_,_)},200,_);
			.print("[ERROR] Trying again to commit to ",M," on ",S);
			commitMission(M)[artifact_name(S)];
			!!check_commit_mission(M,S).



// start new mission
+!start_new_mission(M)
	: obligation(Me,Norm,achieved(Scheme,Goal,Ag),DeadLine) & .my_name(Me)
	<-	.print("Achived goal ", Goal);
			goalAchieved(Goal)[artifact_name(Scheme)];
			.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(Scheme)];
			!check_commit_mission(M,Scheme).
+!start_new_mission(M)
	<-	.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(Scheme)];
			!check_commit_mission(M,Scheme).

/* Plans to finish the simulation and start a new one */

+simEnd
   <- .drop_all_desires;
   		.send(coordinator,tell,simEnd);
			-simEnd;
			-simStart;
			!remove_percepts;
			jia.restart_world_model.

+!remove_percepts
	<-	.abolish(achievement(_));
			.abolish(coworker(_,_,_));
			.abolish(coworkerPosition(_,_));
			.abolish(role(_)).