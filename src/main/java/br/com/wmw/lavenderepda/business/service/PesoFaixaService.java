package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesoFaixa;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesoFaixaDbxDao;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class PesoFaixaService extends CrudService {

	public static final String IMAGES_ICONE_PESOFAIXA_PNG = "images/pesoFaixaIcon.png";
    private static PesoFaixaService instance;

    private PesoFaixaService() {}
	public static PesoFaixaService getInstance() { return (instance == null) ? instance = new PesoFaixaService() : instance; }

    @Override public void validate(BaseDomain domain) {}
    @Override protected CrudDao getCrudDao() { return PesoFaixaDbxDao.getInstance(); }

    private PesoFaixa getPesoFaixaFilter(final String cdEmpresa, final double qtPeso) {
    	if (ValueUtil.isEmpty(cdEmpresa)) return null;
	    PesoFaixa pesoFaixaFilter = new PesoFaixa();
    	pesoFaixaFilter.cdEmpresa = cdEmpresa;
    	pesoFaixaFilter.qtPeso = qtPeso;
    	return pesoFaixaFilter;
    }
    
    private PesoFaixa getPesoFaixaByPedido(final Pedido pedido) throws SQLException {
    	if (pedido == null) return null;
	    PesoFaixa pesoFaixaFilter = getPesoFaixaFilter(pedido.cdEmpresa, pedido.qtPeso);
    	if (pesoFaixaFilter == null) return null;
    	return PesoFaixaDbxDao.getInstance().findPesoFaixaByPeso(pesoFaixaFilter);
    }

    public void applyPesoFaixaInPedido(Pedido pedido) throws SQLException {
	    PesoFaixa pesoFaixa = getPesoFaixaByPedido(pedido);
    	if (pesoFaixa == null) {
    		pedido.pesoFaixa = null;
    		return;
	    }
    	pedido.pesoFaixa = pesoFaixa;
	}

    public Image getIconPesoFaixa(final PesoFaixa pesoFaixa) {
        return UiUtil.getColorfulImage(IMAGES_ICONE_PESOFAIXA_PNG, UiUtil.getLabelPreferredHeight() / 7 * 6, UiUtil.getLabelPreferredHeight() / 7 * 6, (pesoFaixa != null) ? getCorIconePesoFaixa(pesoFaixa) : Color.BRIGHT);
    }
    
    public int getCorIconePesoFaixa(final PesoFaixa pesoFaixa) {
		return (pesoFaixa != null) ? Color.getRGB(pesoFaixa.corSistema.vlR, pesoFaixa.corSistema.vlG, pesoFaixa.corSistema.vlB) : 0;
    }

    public void validateFaixaIdealPedido(Pedido pedido) throws ValidationException, SQLException {
	    Vector listPesoFaixaIdeal = findAllByExample(new PesoFaixa(pedido.cdEmpresa, ValueUtil.VALOR_SIM));
	    int size = listPesoFaixaIdeal.size();
	    if (size == 0) return;
	    String faixasIdeal = "";
	    for (int i = 0; i < size; i ++) {
		    PesoFaixa pesoFaixaIdeal = (PesoFaixa) listPesoFaixaIdeal.items[i];
		    faixasIdeal = pesoFaixaIdeal.qtPeso + Messages.PESO_FAIXA_KG;
	    }
	    if (pedido.pesoFaixa == null || !pedido.pesoFaixa.isFaixaIdeal())
		    throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_FAIXA_NAO_ATINGIU, faixasIdeal));
    }

}