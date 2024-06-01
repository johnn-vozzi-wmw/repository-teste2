package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.JsonFactory;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.NfeEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.RemessaEstoqueDto;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONArray;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;
import totalcross.util.Vector;

public class PedidoEstoqueService extends CrudService {
	
	private static PedidoEstoqueService instance;
	
	public static PedidoEstoqueService getInstance() {
		if (instance == null) {
			instance = new PedidoEstoqueService();
		}
		return instance;
	}

	public boolean gerarRemessa(PedidoEstoqueDto pedidoRemessa) throws ValidationException, SQLException {
		String remessaJsonString = getPedidoEstoqueJson(pedidoRemessa);
		if (sendAndRecebeRemessa(remessaJsonString, false)) {
			Vector itemPedidoEstoqueList = pedidoRemessa.itemPedidoEstoqueList;
			Vector estoqueList = null;
			int size = itemPedidoEstoqueList.size();
			int size2 = 0;
			for (int i = 0; i < size; i++) {
				ItemPedidoEstoqueDto itemPedidoEstoque = (ItemPedidoEstoqueDto)itemPedidoEstoqueList.items[i];
				estoqueList = EstoqueService.getInstance().findEstoquesParaAjusteRemessa(itemPedidoEstoque);
				size2 = estoqueList.size();
				for (int k = 0; k < size2; k++) {
					Estoque estoque = (Estoque) estoqueList.items[k];
					if (estoque.qtEstoque - itemPedidoEstoque.qtRemessa > 0) {
						EstoqueService.getInstance().updateEstoqueInterno(itemPedidoEstoque.cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, -itemPedidoEstoque.qtRemessa
								, Estoque.FLORIGEMESTOQUE_ERP, false, estoque.cdLocalEstoque);
						break;
					} else {
						EstoqueService.getInstance().updateEstoqueInterno(itemPedidoEstoque.cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, -estoque.qtEstoque
								, Estoque.FLORIGEMESTOQUE_ERP, false, estoque.cdLocalEstoque);
						itemPedidoEstoque.qtRemessa -= estoque.qtEstoque;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private String getPedidoEstoqueJson(PedidoEstoqueDto pedidoEstoque) {
		JSONObject pedidoEstoqueJson = new JSONObject(pedidoEstoque);
		Vector itemPedidoRemessaList = pedidoEstoque.itemPedidoEstoqueList;
		int size = itemPedidoRemessaList.size();
		JSONArray itemRemessaListJson = new JSONArray();
		for (int i = 0; i < size; i++) {
			itemRemessaListJson.put(new JSONObject((ItemPedidoEstoqueDto) itemPedidoRemessaList.items[i]));
		}
		pedidoEstoqueJson.put("itemPedidoEstoqueList", itemRemessaListJson);
		String remessaJsonString = pedidoEstoqueJson.toString();
		return remessaJsonString;
	}

	private boolean sendAndRecebeRemessa(String remessaJsonString, boolean devolucao) throws ValidationException {
		try {
			String remessaJson = SyncManager.sendDadosPedidoEstoque(remessaJsonString, devolucao);
			if (!devolucao) {
				RemessaEstoqueDto remessaEstoqueDto = JsonFactory.parse(remessaJson, RemessaEstoqueDto.class);
				RemessaEstoqueService.getInstance().insert(remessaEstoqueDto);
				NfeEstoqueService.getInstance().insert(remessaEstoqueDto.nfeEstoque);
			} else {
				NfeEstoqueDto nfeEstoqueDto = JsonFactory.parse(remessaJson, NfeEstoqueDto.class);
				NfeEstoqueService.getInstance().insert(nfeEstoqueDto);
			}
			return true;
		} catch (JSONException e) {
			throw new ValidationException(devolucao ? Messages.REMESSAESTOQUE_MSG_ERRO_DEVOLUCAO : Messages.REMESSAESTOQUE_MSG_ERRO_REMESSA);
		} catch (ApplicationException e) {
			if (e.getMessage().equalsIgnoreCase("Read Timed Out")) {
				if (UiUtil.showConfirmYesNoMessage(devolucao ? Messages.REMESSAESTOQUE_ERRO_TIMEOUT_DEVOLUCAO : Messages.REMESSAESTOQUE_ERRO_TIMEOUT)) {
					sendAndRecebeRemessa(remessaJsonString, devolucao);
				} else {
					return false;
				}
			} else {
				ExceptionUtil.handle(e);
				throw e;
			}
		} catch (Throwable e) {
			throw new ValidationException(e.getMessage());
		}
		return false;
	}
	
	public void geraDevolucao(boolean parcial, Vector produtoList) throws SQLException {
		PedidoEstoqueDto pedidoEstoque = new PedidoEstoqueDto();
		pedidoEstoque.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoEstoque.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoEstoque.cdPedidoEstoque = RemessaEstoqueService.getInstance().generateIdGlobal();
		pedidoEstoque.flTipoRemessa = PedidoEstoqueDto.TIPOREMESSA_DEVOLUCAO;
		pedidoEstoque.dtPedidoEstoque = DateUtil.getCurrentDate();
		pedidoEstoque.hrPedidoEstoque = TimeUtil.getCurrentTimeHHMMSS();
		int size = produtoList.size();
		Vector itemPedidoEstoqueList = new Vector(size);
		Produto produto = null;
		for (int i = 0; i < size; i++) {
			produto = (Produto) produtoList.items[i];
			ItemPedidoEstoqueDto itemPedidoEstoque = new ItemPedidoEstoqueDto();
			itemPedidoEstoque.cdEmpresa = pedidoEstoque.cdEmpresa;
			itemPedidoEstoque.cdRepresentante = pedidoEstoque.cdRepresentante;
			itemPedidoEstoque.cdPedidoEstoque = pedidoEstoque.cdPedidoEstoque;
			itemPedidoEstoque.cdProduto = produto.cdProduto;
			itemPedidoEstoque.qtRemessa = produto.qtEstoqueProduto;
			itemPedidoEstoqueList.addElement(itemPedidoEstoque);
			if (produto.qtEstoqueProduto < 0) {
				LogAppService.getInstance().logEstoque(produto.cdProduto, "O estoque ficou negativo ao gerar devolução");
			}
		}
		pedidoEstoque.itemPedidoEstoqueList = itemPedidoEstoqueList;
		if (sendAndRecebeRemessa(getPedidoEstoqueJson(pedidoEstoque), true)) {
			if (parcial) {
				EstoqueService.getInstance().updateEstoqueAposDevolucao();
				EstoqueService.getInstance().deleteEstoqueDevolucaoRemessa();
				RemessaEstoqueService.getInstance().deleteRemessasRepresentante(true);
			} else {
				EstoqueService.getInstance().deletaEstoqueAposDevolucaoTotal();
				RemessaEstoqueService.getInstance().deleteRemessasRepresentante(false);
			}
		}
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return null;
	}

}
