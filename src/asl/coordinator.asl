// Agent Coordinator

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start
	<- 	createWorkspace("marsWS");
  		joinWorkspace("marsWS",MarsMWsp);
			makeArtifact("marsGroupBoard","ora4mas.nopl.GroupBoard",["lti-agents-on-mars-os.xml", team, false, false ],GrArtId);
	 		setOwner(coordinator);
     	focus(GrArtId);
     	adoptRole(coordinator)[artifact_id(GrArtId)];
  		!run_scheme(sch1).
-!start[error(I),error_msg(M)]
	<-	.print("Failure in starting! ",I,": ",M).


// scheme creation
+!run_scheme(S)
	<-  makeArtifact(S,"ora4mas.nopl.SchemeBoard",["lti-agents-on-mars-os.xml", marsSch, false, false ],SchArtId);
			focus(SchArtId);
			.print("scheme ",S," created");
			addScheme(S)[artifact_name("marsGroupBoard")]; 
			.print("scheme is linked to responsible group").
-!run_scheme(S)[error(I),error_msg(M)]
	<- 	.print("failure creating scheme ",S," -- ",I,": ",M).

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

/* Plans to finish the simulation and start a new one */
+simEnd[source(marcian1)]
	<-	.drop_all_desires;
			!remove_percepts;
			jia.restart_world_model;
   		lookupArtifact("sch1",SchId);
      destroy[artifact_id(SchId)];
			disposeArtifact(SchId);
      !run_scheme(sch1).

+!remove_percepts
	<-	.abolish(achievement(_));
			.abolish(coworker(_,_,_));
			.abolish(coworkerPosition(_,_));
			.abolish(simEnd);
			.abolish(step(_)).

/* Agents coordination plans */

// coordination goal
+!coordinate_goal
	<-	.print("Starting coordinate goal");
			!coordinate.

+!coordinate
	: step(S)
	<- 	.print("(step: ",S,") Executing action coordination");
			!agents_coordination;
			!wait_next_step(S);
			!!coordinate.

+!coordinate
	<-	.print("No step yet... wait a bit");
      .wait( { +step(_) }, 600, _);
	  	!!coordinate.

+!wait_next_step(S)
	: step(S+1).

+!wait_next_step(S)
	<-	.wait( { +step(_) }, 600, _);
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
	<-	.print("failure in send_target! ",I,": ",M).