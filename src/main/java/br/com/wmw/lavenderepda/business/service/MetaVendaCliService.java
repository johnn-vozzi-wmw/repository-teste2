package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCliGrupo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaVendaCliDbxDao;
import totalcross.util.Vector;

public class MetaVendaCliService extends CrudService {

	private static MetaVendaCliService instance;
	
	private MetaVendaCliService() {
		//--
	}

	public static MetaVendaCliService getInstance() {
		if (instance == null) {
			instance = new MetaVendaCliService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return MetaVendaCliDbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public MetaVendaCli calculaVlMetaVlRealizadoDaMetaVendaCli(MetaVendaCli metaVendaCli) throws SQLException {
		Vector metaVendaCliGrupoList = MetaVendaCliGrupoService.getInstance().findAllMetaVendaCliGrupoByMetaVendaCli(metaVendaCli.cdEmpresa, metaVendaCli.cdRepresentante, metaVendaCli.cdCliente, metaVendaCli.cdMetaVendaCli);
		int size = metaVendaCliGrupoList.size();
		for (int i = 0; i < size; i++) {
			MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) metaVendaCliGrupoList.items[i];
			metaVendaCli.vlMeta += metaVendaCliGrupo.vlMeta;
			if (metaVendaCliGrupo.vlRealizado > metaVendaCliGrupo.vlMeta && !LavenderePdaConfig.consideraTotalRealizadoParaCalculoMeta) {
				metaVendaCli.vlRealizado += metaVendaCliGrupo.vlMeta;
				continue;
			}
			MetaVendaCliGrupoService.getInstance().calculaRealizadoMetaVendaCliGrupo(metaVendaCliGrupo);
			metaVendaCli.vlRealizado += (metaVendaCliGrupo.vlRealizado > metaVendaCliGrupo.vlMeta && !LavenderePdaConfig.consideraTotalRealizadoParaCalculoMeta) ? metaVendaCliGrupo.vlMeta : metaVendaCliGrupo.vlRealizado;
			
		}
		metaVendaCli.pctRealizado = metaVendaCli.getPctRealizadoMeta();
		metaVendaCli.pctRestante = metaVendaCli.getPctRestanteMeta();
		metaVendaCli.vlRealizar = metaVendaCli.vlMeta - metaVendaCli.vlRealizado;
		return metaVendaCli;
	}

	public Vector findAllMetaVendaCli(MetaVendaCli metaVendaCliFilter) throws SQLException {
		return MetaVendaCliDbxDao.getInstance().findAllMetaVendaCli(metaVendaCliFilter);
	}
	
}
