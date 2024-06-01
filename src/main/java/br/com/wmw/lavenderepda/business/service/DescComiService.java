package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DescComi;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescComiDbxDao;
import totalcross.util.Vector;

public class DescComiService extends CrudService {

    private static DescComiService instance;

    private DescComiService() {
        //--
    }

    public static DescComiService getInstance() {
        if (instance == null) {
            instance = new DescComiService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescComiDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public DescComi getDescComissaoProdVigente(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, String cdProduto) throws SQLException {
    	DescComi descComiFilter = new DescComi();
    	descComiFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	descComiFilter.cdProduto = cdProduto;
    	descComiFilter.cdTipoDesCcomi = DescComi.CDTIPODESCCOMI_PROD;
    	descComiFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
    	descComiFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
    	Vector descComiList = findAllByExample(descComiFilter);
    	return getDescComiNivelMaisBaixo(descComiList, cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade);
    }

    public DescComi getDescComissaoGrupoVigente(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
    	DescComi descComiFilter = new DescComi();
    	descComiFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	descComiFilter.cdGrupoProduto1 = cdGrupoProduto1;
    	descComiFilter.cdGrupoProduto2 = cdGrupoProduto2;
    	descComiFilter.cdGrupoProduto3 = cdGrupoProduto3;
    	descComiFilter.cdTipoDesCcomi = DescComi.CDTIPODESCCOMI_GRUPO;
    	descComiFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
    	descComiFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
    	//--
    	if (ValueUtil.isNotEmpty(cdGrupoProduto3)) {
    		Vector descComiList = findAllByExample(descComiFilter);
    		DescComi descComi = getDescComiNivelMaisBaixo(descComiList, cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade);
    		if (descComi != null) {
    			return descComi;
    		}
    	}
    	//--
    	if (ValueUtil.isNotEmpty(cdGrupoProduto2)) {
    		descComiFilter.cdGrupoProduto3 = null;
    		descComiFilter.filterCdGrupoProduto3Null = true;
    		Vector descComiList = findAllByExample(descComiFilter);
    		DescComi descComi = getDescComiNivelMaisBaixo(descComiList, cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade);
    		if (descComi != null) {
    			return descComi;
    		}
    	}
    	//--
    	if (ValueUtil.isNotEmpty(cdGrupoProduto1)) {
    		descComiFilter.cdGrupoProduto2 = null;
    		descComiFilter.filterCdGrupoProduto2Null = true;
    		Vector descComiList = findAllByExample(descComiFilter);
    		DescComi descComi = getDescComiNivelMaisBaixo(descComiList, cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade);
    		if (descComi != null) {
    			return descComi;
    		}
    	}
    	return null;
    }

    private DescComi getDescComiNivelMaisBaixo(Vector descComiList, String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade) {
    	DescComi descComi = null;
    	DescComi descComiTemp;
    	for (int i = 0; i < descComiList.size(); i++) {
    		descComiTemp = (DescComi)descComiList.items[i];
    		if ((ValueUtil.isEmpty(descComiTemp.cdCliente) || ValueUtil.valueEquals(descComiTemp.cdCliente, cdCliente)) &&
    				(ValueUtil.isEmpty(descComiTemp.cdRepresentante) || ValueUtil.valueEquals(descComiTemp.cdRepresentante, cdRepresentante)) &&
    				(ValueUtil.isEmpty(descComiTemp.cdCondicaoPagamento) || ValueUtil.valueEquals(descComiTemp.cdCondicaoPagamento, cdCondicaoPagamento)) &&
    				(ValueUtil.isEmpty(descComiTemp.cdRamoAtividade) || ValueUtil.valueEquals(descComiTemp.cdRamoAtividade, cdRamoAtividade)) &&
    				(ValueUtil.isEmpty(descComiTemp.cdCidade) || ValueUtil.valueEquals(descComiTemp.cdCidade, cdCidade))) {
    			if (descComi == null || (ValueUtil.isEmpty(descComi.cdCliente) && !ValueUtil.isEmpty(descComiTemp.cdCliente)) ||
    					(descComi.cdCliente.equals(descComiTemp.cdCliente) && ValueUtil.isEmpty(descComi.cdRepresentante) && !ValueUtil.isEmpty(descComiTemp.cdRepresentante)) ||
    					(descComi.cdCliente.equals(descComiTemp.cdCliente) && descComi.cdRepresentante.equals(descComiTemp.cdRepresentante) && ValueUtil.isEmpty(descComi.cdCondicaoPagamento) && !ValueUtil.isEmpty(descComiTemp.cdCondicaoPagamento)) ||
    					(descComi.cdCliente.equals(descComiTemp.cdCliente) && descComi.cdRepresentante.equals(descComiTemp.cdRepresentante) && descComi.cdCondicaoPagamento.equals(descComiTemp.cdCondicaoPagamento) && ValueUtil.isEmpty(descComi.cdRamoAtividade) && !ValueUtil.isEmpty(descComiTemp.cdRamoAtividade)) ||
    					(descComi.cdCliente.equals(descComiTemp.cdCliente) && descComi.cdRepresentante.equals(descComiTemp.cdRepresentante) && descComi.cdCondicaoPagamento.equals(descComiTemp.cdCondicaoPagamento) && descComi.cdRamoAtividade.equals(descComiTemp.cdRamoAtividade) && ValueUtil.isEmpty(descComi.cdCidade) && !ValueUtil.isEmpty(descComiTemp.cdCidade))) {
    				descComi = descComiTemp;
    				setStringVaziaParaCamposNulos(descComi);
    			}
    		}
		}
    	return descComi;
    }
    
    private void setStringVaziaParaCamposNulos(DescComi descComi) {
    	if (descComi != null) {
	    	descComi.cdCliente = descComi.cdCliente != null ? descComi.cdCliente : "";
	    	descComi.cdRepresentante = descComi.cdRepresentante != null ? descComi.cdRepresentante : "";
	    	descComi.cdCondicaoPagamento = descComi.cdCondicaoPagamento != null ? descComi.cdCondicaoPagamento : "";
	    	descComi.cdRamoAtividade = descComi.cdRamoAtividade != null ? descComi.cdRamoAtividade : "";
    	}
    }


}