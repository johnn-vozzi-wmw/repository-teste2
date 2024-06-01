package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCliGrupo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaVendaCliGrupoDbxDao;
import totalcross.util.Vector;

public class MetaVendaCliGrupoService extends CrudService {

    private static MetaVendaCliGrupoService instance;

    private MetaVendaCliGrupoService() {
        //--
    }

    public static MetaVendaCliGrupoService getInstance() {
        if (instance == null) {
            instance = new MetaVendaCliGrupoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaVendaCliGrupoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void calculaMetaVendaCliGrupo(Vector metaVendaCliGrupoList) throws SQLException {
    	int size = metaVendaCliGrupoList.size();
    	for (int i = 0; i < size; i++) {
			MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) metaVendaCliGrupoList.items[i];
			if (metaVendaCliGrupo.vlRealizado > metaVendaCliGrupo.vlMeta && !LavenderePdaConfig.consideraTotalRealizadoParaCalculoMeta) {
				metaVendaCliGrupo.vlRealizado = metaVendaCliGrupo.vlMeta;
				continue;
			}
			calculaRealizadoMetaVendaCliGrupo(metaVendaCliGrupo);			
		}
    }

    public Vector findAllMetaVendaCliGrupoByMetaVendaCli(String cdEmpresa, String cdRepresentante, String cdCliente, String cdMetaVendaCli) throws SQLException {
		MetaVendaCliGrupo metaVendaCliGrupoFilter = new MetaVendaCliGrupo();
		metaVendaCliGrupoFilter.cdEmpresa = cdEmpresa;
		metaVendaCliGrupoFilter.cdRepresentante = cdRepresentante;
		metaVendaCliGrupoFilter.cdCliente = cdCliente;
		metaVendaCliGrupoFilter.cdMetaVendaCli = cdMetaVendaCli;
		return MetaVendaCliGrupoService.getInstance().findAllByExample(metaVendaCliGrupoFilter);
	}

    public void calculaRealizadoMetaVendaCliGrupo(MetaVendaCliGrupo metaVendaCliGrupo) throws SQLException {
		metaVendaCliGrupo.vlRealizado += ItemPedidoService.getInstance().findVlTotalItemPedidoMeta(metaVendaCliGrupo.cdGrupoProduto, metaVendaCliGrupo.cdCliente);
		if (metaVendaCliGrupo.vlRealizado > metaVendaCliGrupo.vlMeta && !LavenderePdaConfig.consideraTotalRealizadoParaCalculoMeta) {
			metaVendaCliGrupo.vlRealizado = metaVendaCliGrupo.vlMeta;
			return;
		}
	}


}