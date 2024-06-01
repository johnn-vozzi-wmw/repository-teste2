package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RequisicaoServDbxDao;

public class RequisicaoServService extends CrudService {
	
    private static RequisicaoServService instance;

    public static RequisicaoServService getInstance() {
        if (instance == null) {
            instance = new RequisicaoServService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain;
		if (ValueUtil.isEmpty(requisicaoServ.tipoServ.cdRequisicaoServTipo)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_REQUISICAOSERV, new Object[] {Messages.REQUISICAOSERV_COMBO_TIPO_SERVICO}));
		}
		if (requisicaoServ.obrigaCliente && requisicaoServ.cdCliente == null) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_REQUISICAOSERV, new Object[] {Messages.REQUISICAOSERV_COMBO_CLIENTE}));
		}
		if (requisicaoServ.obrigaPedido && requisicaoServ.nuPedido == null) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_REQUISICAOSERV, new Object[] {Messages.REQUISICAOSERV_COMBO_PEDIDO}));
		}
		if (ValueUtil.isEmpty(requisicaoServ.motivoServ.cdRequisicaoServMotivo)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_REQUISICAOSERV, new Object[] {Messages.REQUISICAOSERV_COMBO_MOTIVO}));
		}
		if (ValueUtil.valueEquals(requisicaoServ.motivoServ.flObrigaObservacao, ValueUtil.VALOR_SIM) && ValueUtil.valueEquals(requisicaoServ.dsObservacao, ValueUtil.VALOR_NI)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_REQUISICAOSERV, new Object[] {Messages.OBSERVACAO_LABEL}));
		}
	}

	@Override
	protected CrudDao getCrudDao() {
		return RequisicaoServDbxDao.getInstance();
	}

	public int findByNewRequisicao(RequisicaoServ requisicaoServFilter) throws SQLException {
		return RequisicaoServDbxDao.getInstance().findByNewRequisicao(requisicaoServFilter);
	}
	
	@Override
	public void insert(BaseDomain domain) throws SQLException {
		super.insert(domain);
		RequisicaoServImagemService.getInstance().insertImagesDatabase((RequisicaoServ) domain);
	}

}
