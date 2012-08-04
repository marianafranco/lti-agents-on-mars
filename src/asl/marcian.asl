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

// marcian1: create workspace and groupBoard and scheme artifacts
+!start
	:	.my_name(marcian1)
	<- 	createWorkspace("marsWS");
  		joinWorkspace("marsWS",MarsMWsp);
			makeArtifact("marsGroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml", team, false, false ],GrArtId);
	 		setOwner(marcian1);
     	focus(GrArtId);
  		!run_scheme(sch1);
  		!playRole.
-!start[error(I),error_msg(M)]
	: .my_name(marcian1)
	<-	.print("failure in starting! ",I,": ",M).

// if not marcian1: join the created workspace and lookup the org artifacts
+!start 
	<- 	!join;
			lookupArtifact("marsGroupBoard",GrId);
			focus(GrId);
			!playRole.
-!start
	<- 	.wait(100);
			!start.

+!join 
	<- 	.my_name(Me);
			joinWorkspace("marsWS",_).
-!join
	<- 	.wait(200);
			!join.

// scheme creation
+!run_scheme(S)
	<- //makeArtifact(S,"ora4mas.nopl.MySchemeBoard",["wp-os.xml", writePaperSch, false, true ],SchArtId);
			makeArtifact(S,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", marsSch, false, false ],SchArtId);
			focus(SchArtId);
			.print("scheme ",S," created");
			addScheme(S)[artifact_name("marsGroupBoard")]; 
			.print("scheme is linked to responsible group").
-!run_scheme(S)[error(I),error_msg(M)]
	<- 	.print("failure creating scheme ",S," -- ",I,": ",M).


// keep focused on schemes my groups are responsible for
+schemes(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.

// plan to start to play a role
+!playRole
	:	role(R) & .my_name(Ag)
	<- 	jia.to_lower_case(R,S);
		  -role(R);
		  +role(S);
			adoptRole(S)[artifact_id(GrArtId)];
			!check_play(Ag,S,_);
			.print("I'll play role ",S);
			!commit_to_mission.

-!playRole
	<- 	.wait(100);
			!playRole.

+!check_play(A,R,_)
	:	play(A,R,_) & .my_name(A).

+!check_play(A,R,_)
	<- 	.wait({+play(_,_,_)},200,_);
			adoptRole(R)[artifact_id(GrArtId)];
			!check_play(A,R,_).


// plans to commit to missions

// explorer
+!commit_to_mission
	:	goalState("sch1",_,_,_,_) & role(explorer)
	<-	.print("I will try to commit to mExplore");
			commitMission(mExplore)[artifact_name(Scheme)];
			!check_commit_mission(mExplore,Scheme).
-!commit_to_mission
	: role(explorer)
	<-	.print("I could not commit to mExplorer");
			.print("I will try to commit to mOccupyZone");
			commitMission(mOccupyZone)[artifact_name(Scheme)];
			!check_commit_mission(mOccupyZone,Scheme).

// repairer
+!commit_to_mission
	:	role(repairer).		// repairers will commit to missions through the obligations handler

// saboteur
+!commit_to_mission
	:	goalState("sch1",_,_,_,_) & role(saboteur)
	<-	.print("I will try to commit to mSabotage");
			commitMission(mSabotage)[artifact_name(Scheme)];
			!check_commit_mission(mSabotage,Scheme).
-!commit_to_mission
	: role(saboteur)
	<-	.print("I could not commit to mSabotage");
			.print("I will try to commit to mOccupyZone");
			commitMission(mOccupyZone)[artifact_name(Scheme)];
			!check_commit_mission(mOccupyZone,Scheme).

// sentinel
+!commit_to_mission
	:	goalState("sch1",_,_,_,_) & role(sentinel)
	<-	.print("I will try to commit to mCoordinate");
			commitMission(mCoordinate)[artifact_name(Scheme)];
			!check_commit_mission(mCoordinate,Scheme).
-!commit_to_mission
	: role(sentinel)
	<-	.print("I could not commit to mCoordinate");
			!commit_to_sabotage_mission.
+!commit_to_sabotage_mission
	:	role(sentinel)
	<-	.print("I will try to commit to mSentinelSabotage");
			commitMission(mSentinelSabotage)[artifact_name(Scheme)];
			!check_commit_mission(mSentinelSabotage,Scheme).
-!commit_to_sabotage_mission
	: role(sentinel)
	<-	.print("I could not commit to mSentinelSabotage");
			.print("I will try to commit to mOccupyZone");
			commitMission(mOccupyZone)[artifact_name(Scheme)];
			!check_commit_mission(mOccupyZone,Scheme).

// inspector
+!commit_to_mission
	:	goalState("sch1",_,_,_,_) & role(inspector)
	<-	.print("I will try to commit to mInspect");
			commitMission(mInspect)[artifact_name(Scheme)];
			!check_commit_mission(mInspect,Scheme).
-!commit_to_mission
	: role(inspector)
	<-	.print("I could not commit to mInspect");
			.print("I will try to commit to mOccupyZone");
			commitMission(mOccupyZone)[artifact_name(Scheme)];
			!check_commit_mission(mOccupyZone,Scheme).

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
	<-	.print("OK!");
			.broadcast(tell,coworker(A,R,M)).			// broadcast

+!check_commit_mission(M,S)
	<-	.wait({+commitment(_,_,_)},200,_);
			.print("[ERROR] Trying again to commit to ",M," on ",S);
			commitMission(Mission)[artifact_name(S)];
			!check_commit_mission(M,S).


// start new mission
+!start_new_mission(M)
	: obligation(Ag,Norm,achieved(Scheme,Goal,Ag),DeadLine)
	<-	.print("Achived goal ", Goal);
			goalAchieved(Goal)[artifact_name(Scheme)];
			.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(Scheme)];
			!check_commit_mission(M,Scheme).
+!start_new_mission(M)
	<-	.print("I will try to commit to ", M);
			commitMission(M)[artifact_name(Scheme)];
			!check_commit_mission(M,Scheme).