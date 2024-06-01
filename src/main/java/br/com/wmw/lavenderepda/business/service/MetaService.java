package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.domain.MetaAcompanhamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class MetaService extends CrudService {

    private static MetaService instance;

    private MetaService() {
        //--
    }

    public static MetaService getInstance() {
        if (instance == null) {
            instance = new MetaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllMetasGroupByPeriodo(String cdEmpresa) throws SQLException {
    	Vector result = new Vector();
    	Meta metaFilter = new Meta();
    	metaFilter.cdEmpresa = cdEmpresa;
    	Vector metaList = findAllByExample(metaFilter);
    	int size = metaList.size();
    	for (int i = 0; i < size; i++) {
    		Meta meta = (Meta)metaList.items[i];
    		meta.cdRepresentante = "";
    		int indexOf = result.indexOf(meta);
    		if (indexOf == -1) {
    			result.addElement(meta);
    			continue;
    		} 
    		Meta metaResult = (Meta)result.items[indexOf];
    		metaResult.qtPedidos += meta.qtPedidos;
    		metaResult.vlRealizadoVendas += meta.vlRealizadoVendas;
    		metaResult.vlMetaVendas += meta.vlMetaVendas;
    		if (!LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) continue;
    		
    		metaResult.qtUnidadeMeta += meta.qtUnidadeMeta;
    		metaResult.qtCaixaMeta += meta.qtCaixaMeta;
    		metaResult.qtMixProdutoMeta += meta.qtMixProdutoMeta;
    		metaResult.qtMixClienteMeta += meta.qtMixClienteMeta;
		}
    	return result;
    }
    
    public Vector filtraMetaParaRelatorioMetaAcompanhamento(Vector metaList , Date dataAtual) {
    	if (ValueUtil.isEmpty(metaList)) return metaList;
			
    	Vector metaListFinal = new Vector(0);
		int size = metaList.size();
		for (int i = 0; i < size; i++) {
	    	Meta meta = (Meta)metaList.items[i];
	    	if (!DateUtil.isBeforeOrEquals(meta.dtInicial, dataAtual) || !DateUtil.isAfterOrEquals(meta.dtFinal, dataAtual)) continue;
    		
	    	metaListFinal.addElement(meta);
		}
		return ValueUtil.isNotEmpty(metaListFinal) ? metaListFinal : metaList; 
    }
    
	public void recalculaMeta(Meta meta, Date dataAtual) {
		if (!LavenderePdaConfig.usaSistemaModoOffLine || meta == null) return; 
		
		if (ValueUtil.isEmpty(meta.metaAcompanhamentoList)) {
			meta.vlRealizadoVendas = 0d;
			meta.qtPedidos = 0;
			return;
		}
		double vlRealizadoMeta = 0d;
		int qtPedidosRealizado = 0;	
		Vector datasMetaDiaria = meta.getDiasUteisPeriodo();
		int diasPeriodo = ValueUtil.isNotEmpty(datasMetaDiaria) ? datasMetaDiaria.size() : 0;
		for (int i = 0; i <= diasPeriodo; i++) {
			Date dtBase = (Date) datasMetaDiaria.items[i];
			if (!DateUtil.isBeforeOrEquals(dtBase, dataAtual)) continue; 
			
			MetaAcompanhamento metaAcompFilter = new MetaAcompanhamento(meta, dtBase);
			int indexMetaAcomp = meta.metaAcompanhamentoList.indexOf(metaAcompFilter);
			if (indexMetaAcomp == -1) continue;
			
			metaAcompFilter = (MetaAcompanhamento) meta.metaAcompanhamentoList.items[indexMetaAcomp];
			vlRealizadoMeta += metaAcompFilter.vlRealizadoMeta;
			qtPedidosRealizado += metaAcompFilter.qtPedidosRealizado;
		}
		meta.vlRealizadoVendas = vlRealizadoMeta;
		meta.qtPedidos = qtPedidosRealizado;
	}
	
    public void recalculaMetaListSup(Meta meta , Date dataAtual) {
    	if (!LavenderePdaConfig.usaSistemaModoOffLine || meta == null) return;
    	
    	meta.metaAcompanhamentoList = meta.metaAcompanhamentoList == null ? new Vector() : meta.metaAcompanhamentoList;
    	double vlRealizadoMeta = 0d;
    	int qtPedidosRealizado = 0;
    	int size = meta.metaAcompanhamentoList.size();
    	for (int i = 0; i < size; i++) {
    		MetaAcompanhamento metaAcomp = (MetaAcompanhamento)meta.metaAcompanhamentoList.items[i];
    		if (!DateUtil.isBeforeOrEquals(metaAcomp.dtRegistro, dataAtual)) continue;
    		
    		if ((metaAcomp.dtRegistro.getDayOfWeek() == 0) || (metaAcomp.dtRegistro.getDayOfWeek() == 6)) continue; 
    		
    		vlRealizadoMeta += metaAcomp.vlRealizadoMeta;
    	  	qtPedidosRealizado += metaAcomp.qtPedidosRealizado;
    	}
    	meta.vlRealizadoVendas = vlRealizadoMeta;
    	meta.qtPedidos = qtPedidosRealizado;
    }

}