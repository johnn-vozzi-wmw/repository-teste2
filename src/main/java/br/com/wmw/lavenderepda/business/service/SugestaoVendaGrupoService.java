package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaGrupo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugestaoVendaGrupoDbxDao;
import totalcross.util.Vector;

public class SugestaoVendaGrupoService extends CrudService {

    private static SugestaoVendaGrupoService instance;

    private SugestaoVendaGrupoService() {
        //--
    }

    public static SugestaoVendaGrupoService getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaGrupoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SugestaoVendaGrupoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {}


    public String getDsSugestaoVenda(SugestaoVendaGrupo sugestaoVendaGrupo) throws SQLException {
    	String dsSugestaoVendaGrupo = GrupoProduto1Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1);
    	dsSugestaoVendaGrupo += "/"+GrupoProduto2Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1, sugestaoVendaGrupo.cdGrupoProduto2);
      if (!GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto3)) {
      	dsSugestaoVendaGrupo += "/"+GrupoProduto3Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1, sugestaoVendaGrupo.cdGrupoProduto2, sugestaoVendaGrupo.cdGrupoProduto3);
      }
    	return dsSugestaoVendaGrupo;
    }

	public boolean isHasSugestaoVendaComQtdePendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		return SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdComQtdPendenteNoPedido(sugestaoVenda, pedido).size() > 0 || findAllSugestaoVendaGrupoComQtdPendenteNoPedido(sugestaoVenda, pedido).size() > 0;
	}

	public Vector findAllSugestaoVendaGrupoComQtdPendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		if (sugestaoVenda == null || ValueUtil.isEmpty(sugestaoVenda.cdSugestaoVenda)) {
			return new Vector(0);
		}
		Vector listFinal = new Vector();
		Vector sugestaoGruposList = findAllSugestaoVendaGrupoBySugestaoVenda(sugestaoVenda);
		forSugestoes: for (int i = 0; i < sugestaoGruposList.size(); i++) {
			SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) sugestaoGruposList.items[i];
			for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
				ItemPedido itemPedido = new ItemPedido();
				itemPedido = ((ItemPedido) pedido.itemPedidoList.items[j]);
				if (ValueUtil.valueEquals(sugestaoVendaGrupo.cdEmpresa, itemPedido.cdEmpresa) && ValueUtil.valueEquals(sugestaoVendaGrupo.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1)
						&& (GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto2)
						|| ValueUtil.valueEquals(sugestaoVendaGrupo.cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto2))
						&& (GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto3)
						|| ValueUtil.valueEquals(sugestaoVendaGrupo.cdGrupoProduto3, itemPedido.getProduto().cdGrupoProduto3))) {
					if (LavenderePdaConfig.ocultaQtItemFisico && LavenderePdaConfig.usaConversaoUnidadesMedida) {
						sugestaoVendaGrupo.qtVendida += ValueUtil.getIntegerValue(itemPedido.qtItemFaturamento);
					} else {
						sugestaoVendaGrupo.qtVendida += ValueUtil.getIntegerValue(itemPedido.getQtItemFisico());
					}
					sugestaoVendaGrupo.qtMixVendida += 1;
					if (sugestaoVendaGrupo.qtUnidadesVenda <= sugestaoVendaGrupo.qtVendida && sugestaoVendaGrupo.qtMixProdutosVenda <= sugestaoVendaGrupo.qtMixVendida) {
						continue forSugestoes;
					}
				}
			}
			listFinal.addElement(sugestaoVendaGrupo);
		}
		//--
		return listFinal;
	}

    public Vector findAllSugestaoVendaGrupoBySugestaoVenda(SugestaoVenda sugestaoVenda) throws SQLException {
    	SugestaoVendaGrupo sugestaoVendaGrupoExample = new SugestaoVendaGrupo();
		sugestaoVendaGrupoExample.cdEmpresa = sugestaoVenda.cdEmpresa;
		sugestaoVendaGrupoExample.cdSugestaoVenda = sugestaoVenda.cdSugestaoVenda;
		return findAllByExample(sugestaoVendaGrupoExample);
    }

}