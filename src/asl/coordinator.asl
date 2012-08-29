// Agent Coordinator

/* Initial beliefs and rules */


/* Initial goals */

!start.

/* Plans */

+!start
	<- 	!add_available_roles;
			createWorkspace("marsWS");
  		joinWorkspace("marsWS",MarsMWsp);
			makeArtifact("marsGroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml",team,false,false],GrArtId);
	 		setOwner(coordinator);
     	focus(GrArtId);
     	makeArtifact("zone1GroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml",zone1_group,false,false],Zone1GrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(Zone1GrArtId)];
     	focus(Zone1GrArtId);
     	makeArtifact("zone2GroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml",zone2_group,false,false],Zone2GrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(Zone2GrArtId)];
     	focus(Zone2GrArtId);
     	makeArtifact("sabotageGroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml",sabotage_group,false,false],SabotageGrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(SabotageGrArtId)];
     	focus(SabotageGrArtId);
     	adoptRole(coordinator)[artifact_id(GrArtId)];
  		!!run_scheme.
-!start[error(I),error_msg(M)]
	<-	.print("Failure in starting! ",I,": ",M).


// scheme creation
+!run_scheme
	<-  makeArtifact(coordinationSch,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", coordinationSch, false, false ],SchArtId);
			focus(SchArtId);
			.print("scheme coordinationSch created");
			addScheme(coordinationSch)[artifact_name("marsGroupBoard")]; 
			//.print("scheme is linked to responsible group");
			makeArtifact(zone1Sch,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", zone1Sch, false, false ],SchArtId1);
			focus(SchArtId1);
			.print("scheme zone1Sch created");
			addScheme(zone1Sch)[artifact_name("zone1GroupBoard")]; 
			//.print("scheme is linked to responsible group");
			makeArtifact(zone2Sch,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", zone2Sch, false, false ],SchArtId2);
			focus(SchArtId2);
			.print("scheme zone2Sch created");
			addScheme(zone2Sch)[artifact_name("zone2GroupBoard")]; 
			//.print("scheme is linked to responsible group");
			makeArtifact(sabotageSch,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", sabotageSch, false, false ],SchArtId3);
			focus(SchArtId3);
			.print("scheme sabotageSch created");
			addScheme(sabotageSch)[artifact_name("sabotageGroupBoard")];
			.broadcast(tell,environment(ok)). 
			//.print("scheme is linked to responsible group").
			
-!run_scheme[error(I),error_msg(M)]
	<- 	.print("failure creating scheme -- ",I,": ",M).

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
	:	.my_name(A) & commitment(A,M,_)
	<-	.print("I commited to ", M).

+!check_commit_mission(M,S)
	<-	.wait({+commitment(_,_,_)},200,_);
			.print("[ERROR] Trying again to commit to ",M," on ",S);
			commitMission(M)[artifact_name(S)];
			!!check_commit_mission(M,S).


/* Plans to inform to the other agents their mission and group to join */
@wtp[atomic]
+want_to_play(R)[source(Ag)]
	: availableRole(Id,R,M,S,G)
	<-	-availableRole(Id,R,M,S,G);
			.send(Ag,tell,availableRole(R,M,S,G)).

+want_to_play(S).


/* Plans to finish the simulation and start a new one */
+simEnd[source(marcian1)]
	<-	.drop_all_desires;
			!remove_percepts;
			//!add_available_roles;
			jia.restart_world_model;
   		lookupArtifact("sabotageSch",SchId);
      destroy[artifact_id(SchId)];
			disposeArtifact(SchId);
			lookupArtifact("zone2Sch",SchId1);
      destroy[artifact_id(SchId1)];
			disposeArtifact(SchId1);
			lookupArtifact("zone1Sch",SchId2);
      destroy[artifact_id(SchId2)];
			disposeArtifact(SchId2);
			lookupArtifact("coordinationSch",SchId3);
      destroy[artifact_id(SchId3)];
			disposeArtifact(SchId3);
      !run_scheme.

+!remove_percepts
	<-	.abolish(achievement(_));
			.abolish(coworker(_,_,_));
			.abolish(coworkerPosition(_,_));
			.abolish(simEnd);
			.abolish(want_to_play(_));
			.abolish(step(_)).

/* Agents coordination plans */

// coordination goal
+!coordinate_goal
	<-	.print("Starting coordinate goal");
			!coordinate.

+!coordinate
	: step(S)
	<- 	.print("(step: ",S,") Executing goal coordination");
			!agents_coordination;
			!wait_next_step(S);
			!!coordinate.

+!coordinate
	<-	//.print("No step yet... wait a bit");
      .wait( { +step(_) }, 300, _);
	  	!!coordinate.

+!wait_next_step(S)
	: step(S+1).

+!wait_next_step(S)
	<-	.wait( { +step(_) }, 300, _);
			!wait_next_step(S).


+!agents_coordination
	<- 	jia.agents_coordination(A,P);
			.print("New formation!! ", .length(P));
			!send_target(A,P).

+!send_target([X|TAg],[Y|TLoc])
 	<- 	.print("send: ",X, ", " ,Y);
 	   	.send(X,tell,target(Y));
 	   	!send_target(TAg,TLoc).
+!send_target([],[]).

-!send_target[error(I),error_msg(M)]
	<-	.print("[ERROR] Failure in send_target! ",I,": ",M).

	
	
	
// available roles in the beginning of the match
// the coordinator is responsible to distribute these roles among the other agents
+!add_available_roles
	<-	+availableRole(1,explorer,mExplore,"zone1Sch","zone1GroupBoard");
			+availableRole(2,explorer,mOccupyZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(3,explorer,mOccupyZone2,"zone2Sch","zone2GroupBoard");
			+availableRole(4,explorer,mOccupyZone2,"zone2Sch","zone2GroupBoard");

			+availableRole(5,repairer,mRepairZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(6,repairer,mRepairZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(7,repairer,mRepairZone2,"zone2Sch","zone2GroupBoard");
			+availableRole(8,repairer,mRepairZone2,"zone2Sch","zone2GroupBoard");

			+availableRole(9,saboteur,mSabotage,"sabotageSch","sabotageGroupBoard");
			+availableRole(10,saboteur,mOccupyZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(11,saboteur,mOccupyZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(12,saboteur,mOccupyZone2,"zone2Sch","zone2GroupBoard");

			+availableRole(13,sentinel,mSentinelSabotage,"sabotageSch","sabotageGroupBoard");
			+availableRole(14,sentinel,mOccupyZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(15,sentinel,mOccupyZone2,"zone2Sch","zone2GroupBoard");
			+availableRole(16,sentinel,mOccupyZone2,"zone2Sch","zone2GroupBoard");

			+availableRole(17,inspector,mInspect,"zone1Sch","zone1GroupBoard");
			+availableRole(18,inspector,mOccupyZone1,"zone1Sch","zone1GroupBoard");
			+availableRole(19,inspector,mOccupyZone2,"zone2Sch","zone2GroupBoard");
			+availableRole(20,inspector,mOccupyZone2,"zone2Sch","zone2GroupBoard").