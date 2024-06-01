package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocionalGrade;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescPromocionalGradeDbxDao;
import totalcross.util.Vector;

public class DescPromocionalGradeService extends CrudService {
	
	public static DescPromocionalGradeService instance;
	
	public static DescPromocionalGradeService getInstance() {
		if (instance == null) {
			instance = new DescPromocionalGradeService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DescPromocionalGradeDbxDao.getInstance();
	}
	
	public boolean validateQtItemGradeMultiplos(Vector itemPedidoGrade2List, Vector descPromoGradeList, Vector itemGradeList, ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(descPromoGradeList)) {
			int size = itemPedidoGrade2List.size();
			Map<String, Double> maiorMultiplicador = new HashMap<>();
			Map<String, Map<String, ItemPedidoGrade>> mapPorNivel1 = new HashMap<>();
			for (int i = 0; i < size; i++) {
				ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade)itemPedidoGrade2List.items[i];
					Double maiorMult = maiorMultiplicador.get(itemPedidoGrade.cdItemGrade1);
					if (maiorMult != null) {
						double multEspecial = findMultiplicadorItem(itemPedido, itemPedidoGrade.cdItemGrade2);
						if (multEspecial <= 0) {
							continue;
						}
						double multItem = itemPedidoGrade.qtItemFisico / multEspecial;
						multItem = multItem < 1 ? 1 : multItem;
						if (multItem >= 1) {
							multItem = ValueUtil.getIntegerValueRoundUp(multItem);
							maiorMult = maiorMult > multItem ? maiorMult : multItem;
							maiorMultiplicador.put(itemPedidoGrade.cdItemGrade1, maiorMult);
						}
					} else {
						double multEspecial = findMultiplicadorItem(itemPedido, itemPedidoGrade.cdItemGrade2);
						if (multEspecial <= 0) {
							continue;
						}
						double multItem = itemPedidoGrade.qtItemFisico / multEspecial;
						multItem = multItem < 1 ? 1 : multItem;
						if (multItem >= 1) {
							multItem = ValueUtil.getIntegerValueRoundUp(multItem);
							maiorMultiplicador.put(itemPedidoGrade.cdItemGrade1, multItem);
						}
					}
					Map<String, ItemPedidoGrade> listItensGrade2 = mapPorNivel1.get(itemPedidoGrade.cdItemGrade1); 
					if (listItensGrade2 == null) {
						listItensGrade2 = new HashMap<>();
						mapPorNivel1.put(itemPedidoGrade.cdItemGrade1, listItensGrade2);
					}
					listItensGrade2.put(itemPedidoGrade.cdItemGrade2, itemPedidoGrade);
			}
			size = descPromoGradeList.size();
			for (Map.Entry<String, Map<String, ItemPedidoGrade>> entryNivel2 : mapPorNivel1.entrySet()) {
				Map<String, ItemPedidoGrade> mapNivel2 = entryNivel2.getValue();
				double multiplicador = maiorMultiplicador.get(entryNivel2.getKey());
				for (int i = 0; i < size; i++) {
					DescPromocionalGrade descPromocionalGrade = (DescPromocionalGrade) descPromoGradeList.items[i];
					ItemPedidoGrade itemGrade2 = mapNivel2.get(descPromocionalGrade.cdItemGrade2);
					if (itemGrade2 == null) {
						itemGrade2 = (ItemPedidoGrade)mapNivel2.values().toArray()[0];
						throw new ValidationException(MessageUtil.getMessage(Messages.GRADE_FECHADA_ERRO_MULTIPLOS, new Object[] {TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGrade2.itemGrade1.cdTipoItemGrade) + " - " + ItemGradeService.getInstance().getDsItemGrade(itemGrade2.itemGrade1.cdTipoItemGrade, itemGrade2.cdItemGrade1), 
								getGradeMsgMultiplo(itemGradeList, descPromoGradeList, new Vector(mapNivel2.values().toArray()) , multiplicador)}));
					}
					if (itemGrade2 != null && itemGrade2.qtItemFisico % descPromocionalGrade.nuMultiploEspecial >  1E-7 || itemGrade2.qtItemFisico % multiplicador > 1E-7 || (itemGrade2.qtItemFisico % multiplicador < 1E-7 && itemGrade2.qtItemFisico < descPromocionalGrade.nuMultiploEspecial * multiplicador)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.GRADE_FECHADA_ERRO_MULTIPLOS, new Object[] {TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGrade2.itemGrade1.cdTipoItemGrade) + " - " + ItemGradeService.getInstance().getDsItemGrade(itemGrade2.itemGrade1.cdTipoItemGrade, itemGrade2.cdItemGrade1), 
								getGradeMsgMultiplo(itemGradeList, descPromoGradeList, new Vector(mapNivel2.values().toArray()), multiplicador)}));
					}
				}
			}
			return !mapPorNivel1.isEmpty();
		}
		return true;
	}
	
	private double findMultiplicadorItem(ItemPedido itemPedido, String cdGrade2)  throws SQLException {
		DescPromocionalGrade descPromocionalGrade = new DescPromocionalGrade();
		descPromocionalGrade.descPromocional = itemPedido.getDescPromocional();
		descPromocionalGrade.cdEmpresa = itemPedido.cdEmpresa;
		descPromocionalGrade.cdRepresentante = itemPedido.cdRepresentante;
		descPromocionalGrade.cdItemGrade2 = cdGrade2;
		String nuMultEspStr = DescPromocionalGradeDbxDao.getInstance().findColumnByRowKey(descPromocionalGrade.getRowKey(), "nuMultiploEspecial");
		if (ValueUtil.isEmpty(nuMultEspStr) || nuMultEspStr.equals("0")) { 
			return -1;
		}
		return ValueUtil.getDoubleValue(DescPromocionalGradeDbxDao.getInstance().findColumnByRowKey(descPromocionalGrade.getRowKey(), "nuMultiploEspecial"));
	}
	
	private String getGradeMsgMultiplo(Vector listItemGrade2, Vector descPromocionalList, Vector itemPedidoGrade2List, double multip) throws SQLException {
		boolean ordenaGrades = LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto;
		StringBuffer msg = new StringBuffer();
		Vector itensGrade = new Vector(listItemGrade2.size());
		itensGrade.addElementsNotNull(listItemGrade2.items);
		Vector itemGradeOrder = new Vector();
		int size = itemPedidoGrade2List.size();
		int sizeDesc = descPromocionalList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoGrade itemMsg = (ItemPedidoGrade) itemPedidoGrade2List.items[i];
			for (int j = 0; j < sizeDesc; j++) {
				DescPromocionalGrade descPromo = (DescPromocionalGrade)descPromocionalList.items[j];
				if (descPromo.cdItemGrade2.equals(itemMsg.itemGrade2.cdItemGrade)) {
					double qtComMultiplo = descPromo.nuMultiploEspecial * multip;
					itensGrade.removeElement(itemMsg.itemGrade2);
					if (qtComMultiplo - itemMsg.qtItemFisico > 1E-7) {
						if (ordenaGrades) {
							itemMsg.itemGrade2.qtItemGrade = qtComMultiplo;
							itemGradeOrder.addElement(itemMsg.itemGrade2);
						} else {
							if (msg.length() > 0) {
								msg.append("; ").append(itemMsg.itemGrade2.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(qtComMultiplo, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
							} else {
								msg.append(itemMsg.itemGrade2.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(qtComMultiplo, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
							}
						}
					}
					break;
				}
			}
		}
		if (ValueUtil.isNotEmpty(itensGrade)) {
			size = itensGrade.size();
			for (int i = 0; i < size; i++) {
				ItemGrade itemMsg = (ItemGrade) itensGrade.items[i];
				for (int j = 0; j < sizeDesc; j++) {
					DescPromocionalGrade descPromo = (DescPromocionalGrade)descPromocionalList.items[j];
					if (descPromo.cdItemGrade2.equals(itemMsg.cdItemGrade)) {
						double qtComMultiplo = descPromo.nuMultiploEspecial * multip;
						if (ordenaGrades) {
							itemMsg.qtItemGrade = qtComMultiplo;
							itemGradeOrder.addElement(itemMsg);
						} else {
							if (msg.length() > 0) {
								msg.append("; ").append(itemMsg.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(qtComMultiplo, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
							} else {
								msg.append(itemMsg.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(qtComMultiplo, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
							}
						}
						break;
					}
				}
			}
		}
		if (ordenaGrades) {
			SortUtil.qsortInt(itemGradeOrder.items, 0, itemGradeOrder.size() - 1, true);
			for (int i = 0, size2 = itemGradeOrder.size(); i < size2; i++) {
				ItemGrade item = (ItemGrade)itemGradeOrder.items[i];
				if (msg.length() > 0) {
					msg.append("; ").append(item.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(item.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
				} else {
					msg.append(item.dsItemGrade).append(" = ").append(StringUtil.getStringValueToInterface(item.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
				}
			}
		}
		return msg.toString();
	}
	

}
