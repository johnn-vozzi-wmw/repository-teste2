package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.KitTabPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.KitPdbxDao;
import totalcross.util.Vector;

public class KitService extends CrudService {

	private static KitService instance;

	private KitService() {
		//--
	}

	public static KitService getInstance() {
		if (KitService.instance == null) {
			KitService.instance = new KitService();
		}
		return KitService.instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return KitPdbxDao.getInstance();
	}

	//@Override
	public void validate(final BaseDomain domain) {
	}

	public String getDsKit(String cdKit) throws SQLException {
		Kit kit = initializeKit(cdKit, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, Kit.CD_TABELA_PRECO_ZERO);
		kit.dsKit = findColumnByRowKey(kit.getRowKey(), "DSKIT");
		return kit.toString();
	}

	public String getDsKit(String cdKit, Pedido pedido) throws SQLException {
		Kit kit = initializeKit(cdKit, pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdTabelaPreco);
		kit.dsKit = findColumnByRowKey(kit.getRowKey(), "DSKIT");
		return kit.toString();
	}

	public Kit getKit(String cdKit) throws SQLException {
		Kit kit = initializeKit(cdKit, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, Kit.CD_TABELA_PRECO_ZERO);
		return (Kit) findByRowKey(kit.getRowKey());
	}

	public Kit getKit(String cdKit, Pedido pedido) throws SQLException {
		Kit kit = initializeKit(cdKit, pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdTabelaPreco);
		return (Kit) findByRowKey(kit.getRowKey());
	}
	
	public Kit getKit(ItemKit itemKit) throws SQLException {
		Kit kit = initializeKit(itemKit.cdKit, itemKit.cdEmpresa, itemKit.cdRepresentante, itemKit.cdTabelaPreco);
		return (Kit) findByRowKey(kit.getRowKey());
	}

	private Kit initializeKit(String cdKit, String cdEmpresa, String cdRepresentante, String cdTabelaPreco) {
		Kit kit = new Kit();
		kit.cdEmpresa = cdEmpresa;
		kit.cdRepresentante = cdRepresentante;
		kit.cdKit = cdKit;
		kit.cdTabelaPreco = cdTabelaPreco;
		return kit;
	}

	public Vector findAllByTabPreco(String cdTabelaPreco) throws SQLException {
		KitTabPreco kitTabPrecoFilter = new KitTabPreco();
		kitTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		kitTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		kitTabPrecoFilter.cdTabelaPreco = cdTabelaPreco;
		Vector kitTabPrecoList = KitTabPrecoService.getInstance().findAllByExample(kitTabPrecoFilter);
		//--
		int size = kitTabPrecoList.size();
		Vector result = new Vector(size);
		KitTabPreco kitTabPreco;
		for (int i=0; i< size; i++) {
			kitTabPreco = (KitTabPreco) kitTabPrecoList.items[i];
			Kit kit = getKit(kitTabPreco.cdKit);
			if ((kit != null) && kit.isVigente()) {
				result.addElement(kit);
			}
		}
		return result;
	}

	public boolean isKitExiste(String cdKit) throws SQLException {
		return getKit(cdKit) != null;
	}

	public boolean isEditaDescontoKit(String cdKit) throws SQLException {
		Kit kit = getKit(cdKit);
		return kit != null && kit.isEditaDesconto();
	}

	public boolean kitJaFoiAdicionado(Pedido pedido, String cdKit) throws SQLException {
		ItemPedido itemPedidoFilter = ItemPedidoService.getInstance().createNewItemPedido(pedido);
		itemPedidoFilter.cdKit = cdKit;
		return ItemPedidoPdbxDao.getInstance().countItemPedidoPertencemAoKit(itemPedidoFilter) > 0;
	}

	public void loadVlBaseByItemKit(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		ItemKit itemKit = itemPedido.getItemKit();
		if (itemKit == null) return;
		itemPedido.vlBaseItemPedido = itemPedido.vlItemPedido = itemKit.vlUnitarioKit;
		ItemPedidoService.getInstance().applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		ItemPedidoService.getInstance().aplicaIndiceFinanceiroSupRep(itemPedido);
	}
	
	public Vector loadKitListForCombo(Pedido pedido) throws SQLException {
		Kit kit = new Kit();
		kit.cdEmpresa = pedido.cdEmpresa;
		kit.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		kit.cdTabelaPreco = pedido.cdTabelaPreco;		
		kit.dtVigenciaInicial = DateUtil.getCurrentDate();
		kit.dtVigenciaFinal = DateUtil.getCurrentDate();
		kit.cdLocalEstoque = LavenderePdaConfig.isUsaLocalEstoque() ? pedido.getCdLocalEstoque() : Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		Vector kitList = KitPdbxDao.getInstance().findKitListForCombo(kit, pedido.cdCliente, pedido.nuPedido);
		return kitList;
	}
	
	public Vector loadKitListForKit1e2(Pedido pedido) throws SQLException {
		Kit kit = new Kit();
		kit.cdEmpresa = pedido.cdEmpresa;
		kit.cdRepresentante = pedido.cdRepresentante;
		kit.cdTabelaPreco = TabelaPreco.CDTABELAPRECO_VALOR_ZERO;
		kit.dtVigenciaInicial = DateUtil.getCurrentDate();
		kit.dtVigenciaFinal = kit.dtVigenciaInicial;
		return KitPdbxDao.getInstance().findListKitFechado(kit, pedido.cdTabelaPreco);
		
	}

	public Vector findKitsVigenciaExtrapoladaByPedido(Pedido pedido) throws SQLException {
		return KitPdbxDao.getInstance().findKitsVigenciaExtrapoladaByPedido(pedido);
	}
}