package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.HistoricoPedidoDbxDao;

public class HistoricoPedidoService extends CrudService {

    private static HistoricoPedidoService instance;
    
    private HistoricoPedidoService() {
        //--
    }
    
    public static HistoricoPedidoService getInstance() {
        if (instance == null) {
            instance = new HistoricoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return HistoricoPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        HistoricoPedido historicoPedido = (HistoricoPedido) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(historicoPedido.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(historicoPedido.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_CDREPRESENTANTE);
        }
        //nuPedido
        if (ValueUtil.isEmpty(historicoPedido.nuPedido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_NUPEDIDO);
        }
        //cdStatus
        if (ValueUtil.isEmpty(historicoPedido.cdStatus)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_CDSTATUS);
        }
        //flOrigemPedido
        if (ValueUtil.isEmpty(historicoPedido.flOrigemPedido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_FLORIGEMPEDIDO);
        }
        //dtAtualizacao
        if (ValueUtil.isEmpty(historicoPedido.dtAtualizacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_DTATUALIZACAO);
        }
        //hrAtualizacao
        if (ValueUtil.isEmpty(historicoPedido.hrAtualizacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_HRATUALIZACAO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(historicoPedido.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(historicoPedido.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(historicoPedido.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.HISTORICOPEDIDO_LABEL_CDUSUARIO);
        }
*/
    }

}