package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaCadastro;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AgendaCadastroDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class AgendaCadastroService extends CrudService {

    private static AgendaCadastroService instance;

    private AgendaCadastroService() {
        //--
    }

    public static AgendaCadastroService getInstance() {
        if (instance == null) {
            instance = new AgendaCadastroService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AgendaCadastroDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws SQLException {
    	AgendaCadastro agendaCadastro = (AgendaCadastro) domain;
    	if (agendaCadastro.nuDiaSemana == -1) {
    		throw new ValidationException(Messages.MSG_DIA_SEMANA_NAO_INFORMADO);
    	}
    	if (ValueUtil.isNotEmpty(agendaCadastro.hrAgenda) && !TimeUtil.isValidTimeHHMM(agendaCadastro.hrAgenda)) {
    		throw new ValidationException(Messages.MSG_HORA_AGENDA_INVALIDA);
    	}
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita && ValueUtil.isEmpty(agendaCadastro.cdTipoAgenda)) {
    		throw new ValidationException(Messages.MSG_AGENDAVISITA_TIPO_AGENDA_NAO_INFORMADO);
    	}
    	if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
    		if (AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL.equals(agendaCadastro.flTipoFrequencia)) {
    			if (ValueUtil.isNotEmpty(agendaCadastro.dtBase)) {
    				Date date = isReagendamento(agendaCadastro) ? agendaCadastro.dtFinal : agendaCadastro.dtBase;
    				agendaCadastro.nuDiaSemana = DateUtil.getDayOfWeek(date) + 1;
    				agendaCadastro.flSemanaMes = StringUtil.getStringValue(AgendaVisitaService.getInstance().getFlSemanaMes(date));
    				agendaCadastro.flAgendaReagendada = ValueUtil.VALOR_SIM.equals(agendaCadastro.flAgendaReagendada) ? null : agendaCadastro.flAgendaReagendada;
    			} else {
    				throw new ValidationException(Messages.MSG_DATA_INICIO_NAO_INFORMADO);
    			}
    		}
    	}
    	validateDataFinalAgendaVisita(agendaCadastro);
    	validateAgendaDuplicadaDiaSemanaMes(agendaCadastro);
    	if (!LavenderePdaConfig.realizaCadastroAgendaVisitaAoReagendar) {
    	verificaSeAgendaVisitaJaExiste(agendaCadastro.getRowKey());
    	}
    	validaDataAgendaVisitaNoFinalDeSemana(agendaCadastro.nuDiaSemana);
    }

	private boolean isReagendamento(AgendaCadastro agendaCadastro) {
		return ValueUtil.VALOR_SIM.equals(agendaCadastro.flAgendaReagendada) && ValueUtil.isNotEmpty(agendaCadastro.dtFinal);
	}
    
	//@Override
    public void insert(BaseDomain domain) throws SQLException {
    	if (((AgendaCadastro)domain).atualizaAgenda) {
    		try {
    			super.insert((AgendaCadastro)domain);
			} catch (Throwable e) {
				super.update((AgendaCadastro)domain);
			}
    		AgendaVisitaService.getInstance().update((AgendaVisita)domain);
    	} else {
    		super.insert((AgendaCadastro)domain);
    		try {
    			AgendaVisitaService.getInstance().validateDuplicated(domain);
    			AgendaVisitaService.getInstance().insert((AgendaVisita)domain);
    		} catch (Throwable e) { }
    	}
    }
    
    private void validateAgendaDuplicadaDiaSemanaMes(final AgendaCadastro agendaCadastro) throws SQLException {
        if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() && !LavenderePdaConfig.realizaCadastroAgendaVisitaAoReagendar && !AgendaVisitaService.getInstance().isPermiteMultAgendasNoDiaMesmoCliente()) {
            AgendaVisita agendaVisitaFilter = new AgendaVisita();
            agendaVisitaFilter.cdEmpresa = agendaCadastro.cdEmpresa;
            agendaVisitaFilter.cdRepresentante = agendaCadastro.cdRepresentante;
            agendaVisitaFilter.cdCliente = agendaCadastro.cdCliente;
            agendaVisitaFilter.nuDiaSemana = agendaCadastro.nuDiaSemana;
            if (!ValueUtil.valueEquals(ValueUtil.VALOR_SIM, agendaCadastro.flSemanaMes)) {
            	agendaVisitaFilter.flSemanaMes = ValueUtil.VALOR_SIM;
            }
            Vector agendaVisitaList = AgendaVisitaService.getInstance().findAllByExample(agendaVisitaFilter);
            if (ValueUtil.isNotEmpty(agendaVisitaList)) {
                String flSemanaMes = agendaCadastro.flSemanaMes;
                if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, agendaCadastro.flSemanaMes)) {
                	AgendaVisita agendaVisita = (AgendaVisita) agendaVisitaList.items[0];
                    flSemanaMes = agendaVisita.flSemanaMes;
                }
                if (ValueUtil.valueEquals("S", flSemanaMes)) {
                	throw new ValidationException(MessageUtil.getMessage(Messages.MSG_AGENDAVISITA_CLIENTE_POSSUI_AGENDA_SEMANAVIGENTE, new Object[]{DateUtil.getDiaSemana(agendaCadastro.nuDiaSemana - 1)}));
                }
                throw new ValidationException(MessageUtil.getMessage(Messages.MSG_AGENDAVISITA_CLIENTE_POSSUI_AGENDA_DIASEMANA, new Object[]{DateUtil.getDiaSemana(agendaCadastro.nuDiaSemana - 1), flSemanaMes}));
            }
        }
    }
    
    private void validateDataFinalAgendaVisita(AgendaCadastro agendaCadastro) {
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita && ValueUtil.isNotEmpty(agendaCadastro.dtFinal)) {
				if (agendaCadastro.dtFinal.isBefore(DateUtil.getCurrentDate())) {
					if (LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica && ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, agendaCadastro.flTipoFrequencia)) {
						throw new ValidationException(Messages.MSG_DATA_BASE_INFERIOR_DATA_ATUAL);
					}
					throw new ValidationException(Messages.MSG_DATA_FINAL_INFERIOR_DATA_ATUAL);
				}
			}
		}
	}
    
    private void verificaSeAgendaVisitaJaExiste(String rowKey) throws SQLException {
    	if (AgendaVisitaService.getInstance().findByRowKey(rowKey) != null) {
    		throw new ValidationException(Messages.MSG_AGENDA_VISITA_EXISTENTE);
    	}
    }
    
    private void validaDataAgendaVisitaNoFinalDeSemana(int nuDiaSemana) {
    		nuDiaSemana--;
    		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == nuDiaSemana) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_DOMINGO_NAO_PERMITIDO);
    		} else if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == nuDiaSemana) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_SABADO_NAO_PERMITIDO);
    		} else if (LavenderePdaConfig.isAgendaVisitaFinalDeSemanaNaoHabilitado() && (DateUtil.DATA_SEMANA_DOMINGO == nuDiaSemana || DateUtil.DATA_SEMANA_SABADO == nuDiaSemana)) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_FINAL_SEMANA_NAO_PERMITIDO);
    		}
    	}
    
    public AgendaCadastro findAgendaCadastroByAgendaVisita(AgendaVisita agendaVisita) throws SQLException {
    	return (AgendaCadastro) findByRowKey(agendaVisita.getRowKey());
    }
 
    public boolean verificaSeAgendaCadastroJaExiste(AgendaCadastro agendaCadastro) throws SQLException {
    	return findByRowKey(agendaCadastro.getRowKey()) != null;
    }
    
    public void insertOrUpdateAgendaCadastro(AgendaCadastro agendaCadastro) throws SQLException {
    	if (verificaSeAgendaCadastroJaExiste(agendaCadastro)) {
			update(agendaCadastro, true);
		} else {
			insert(agendaCadastro);
		}
    }

    public void excluiAgendaCadastroDataFinalUltrapassada() {
    	AgendaCadastroDbxDao.getInstance().excluiAgendaVisitaDataFinalUltrapassada();
    }
 
}
