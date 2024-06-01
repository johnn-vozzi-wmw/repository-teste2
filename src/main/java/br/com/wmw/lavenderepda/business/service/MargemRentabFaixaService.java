package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MargemRentabFaixaDao;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class MargemRentabFaixaService extends CrudService {

    private static MargemRentabFaixaService instance = null;
    
    private MargemRentabFaixaService() {
    
    }
    
    public static MargemRentabFaixaService getInstance() {
        if (instance == null) {
            instance = new MargemRentabFaixaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MargemRentabFaixaDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException { }
	
	public MargemRentabFaixa findMargemRentabFaixa(Pedido pedido) throws SQLException {
		if (pedido.cdMargemRentab == null) return null;
		return findMargemRentabFaixa(pedido.cdEmpresa, pedido.cdMargemRentab, pedido.vlPctMargemRentab);
	}
	
	public MargemRentabFaixa findMargemRentabFaixa(String cdEmpresa, String cdMargemRentab, double vlPctMargemRentab) throws SQLException {
		if (ValueUtil.isEmpty(cdMargemRentab)) return null;
		MargemRentabFaixa margemRentabFaixaFilter = new MargemRentabFaixa(cdEmpresa, cdMargemRentab, vlPctMargemRentab);
		Vector margemRentabFaixaList =  findAllByExample(margemRentabFaixaFilter);
		return ValueUtil.isNotEmpty(margemRentabFaixaList) ? (MargemRentabFaixa) margemRentabFaixaList.items[0] : null;
	}
	
	public int findCorMargemRentabFaixa(String cdEmpresa, String cdMargemRentab, double vlPctMargemRentab) throws SQLException {
		MargemRentabFaixa margemRentabFaixa = findMargemRentabFaixa(cdEmpresa, cdMargemRentab, vlPctMargemRentab);
		if (margemRentabFaixa != null) {
			return getCorMargemRentabFaixa(margemRentabFaixa.cdCorFaixa);
		}
		return Color.BRIGHT;
	}
	
	public int getCorMargemRentabFaixa(int cdCorFaixa) throws SQLException {
		TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
		int cdEsquemaCor = tema == null ? TemaSistema.CD_ESQUEMA_COR_PADRAO : tema.cdEsquemaCor;
		CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(cdEsquemaCor, cdCorFaixa);
		if (corSistema != null) {
			return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
		}
		return Color.BRIGHT;
	}

}