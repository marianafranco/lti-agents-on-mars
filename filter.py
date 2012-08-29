######################################
#### Filtro para o log da partida ####
####  Por Luciano Menasce Rosset  ####
######################################

from string import find

## FUNCOES ##

def get_agentNum (line):
    if (line[9] == ']'):
        return int(line[8])
    return int( ''+line[8]+line[9] )

def get_step (line):
    if not ("step" in line) and ("yet" in line):
        return -1
    # primeiro indice do numero do step em line
    s = 7 + find(line, "(step: ", 0, len(line))
    # primeiro indice apos o numero do step
    f = find(line, ") E", s, len(line))
    step = ''
    for i in range(s,f):
        step = step+line[i]
    return int(step)

def classe_certa (agentNum, classe, agentes): # Verifica se o report vem da classe a ser filtrada
    if (classe == 'e' and agentNum in agentes[0]):
        return True
    elif (classe == 'i' and agentNum in agentes[1]):
        return True
    elif (classe == 'r' and agentNum in agentes[2]):
        return True
    elif (classe == 's' and agentNum in agentes[3]):
        return True
    elif (classe == 'S' and agentNum in agentes[4]):
        return True
    return False

def filtrandoAcoes(filtro_acoes, acao, line, arq):
    if not ("step" in line):
        return arq
    if (filtro_acoes == 2 and "action" in line):
        arq = arq+line
    elif (filtro_acoes == 3 and "action" in line and acao in line):
        arq = arq+line
    elif (filtro_acoes == 1):
        arq = arq+line
    return arq

## MAIN ##

if (raw_input("Ler mas-0.log como arquivo de entrada? (s/n): ") == "s"):
    log = open("mas-0.log", 'r')
else:
    log = open(raw_input("Nome do arquivo de entrada: "), 'r')


agentes = [[],[],[],[],[]]
# agentes eh uma tabela (classe x num. do agente)
# - 'e' = explorer
# - 'i' = inspector
# - 'r' = repairer
# - 's' = sentinel
# - 'S' = saboteur

i = 0
for line in log:
    if (i == 20):
        break
    if ("I'll play role" in  line):
        ag = get_agentNum(line)
        if ("explorer" in line):
            agentes[0].append(ag)
            i += 1
        elif ("inspector" in line):
            agentes[1].append(ag)
            i += 1
        elif ("repairer" in line):
            agentes[2].append(ag)
            i += 1
        elif ("sentinel" in line):
            agentes[3].append(ag)
            i += 1
        else: # "saboteur" in line
            agentes[4].append(ag)
            i += 1


# Setup do filtro #
step_i = int(raw_input("Comecar filtro por qual step? "))
step_f = int(raw_input("Terminar filtro por qual step? "))

filtro_agente = int(raw_input("Como filtrar os agentes?\n  1: todos os agentes\n  2: apenas uma classe de agente\n  3: apenas um agente\n"))
if (filtro_agente == 2):
    classe = raw_input("Qual classe? (e/i/r/s/S) ('s' = sentinel; 'S' = saboteur)\n")
elif (filtro_agente == 3):
    agente = int(raw_input("Qual o numero do agente? "))

filtro_coordinator = int(raw_input("Incluir coordinator?\n  1: sim\n  2: nao\n"))

filtro_acoes  = int(raw_input("Como filtrar as acoes?\n  1: tudo\n  2: apenas as acoes\n  3: apenas um tipo de acao\n"))
acao = ''
if (filtro_acoes == 3):
    acao = raw_input("Qual acao filtrar? (probe, recharge, parry, buy, goto...)\n")


# Filtrando o log #
current_step = 0
arq = '' # Arquivo de saida
for line in log:
    if ("step:" in line): # Atualiza o step
        current_step = get_step(line)
        if (current_step > step_f):
            break
        
    if (current_step > step_i and filtro_coordinator == 1 and "[co" in line):
        # Checa se eh um report do coordinator
        arq = arq+line
        
    elif (current_step > step_i and "[ma" in line):
        if (filtro_agente == 2 and classe_certa(get_agentNum(line),classe,agentes)):
            arq = filtrandoAcoes(filtro_acoes, acao, line, arq)
            
        elif (filtro_agente == 3 and get_agentNum(line) == agente):
            arq = filtrandoAcoes(filtro_acoes, acao, line, arq)
            
        elif (filtro_agente == 1):
            arq = filtrandoAcoes(filtro_acoes, acao, line, arq)


log.close()
saida = open(raw_input("Nome do arquivo de saida: "),'w')
saida.write(arq)
saida.close()
