package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.Unidade;


public class ConversaoUnidadeService {

	private static ConversaoUnidadeService instance;

    private ConversaoUnidadeService() {
        //--
    }

    public static ConversaoUnidadeService getInstance() {
        if (instance == null) {
            instance = new ConversaoUnidadeService();
        }
        return instance;
    }

    private boolean validarIgnoraMultiploEspecial(ItemPedido itemPedido, Pedido pedido) throws SQLException {
    	if (!LavenderePdaConfig.consisteConversaoUnidadesIgnoraMultiploEspecial()) return false;
    	
		Unidade unidade = itemPedido.getProduto().getUnidade();
    	TipoPedido tipoPedido = pedido.getTipoPedido();
		if (unidade != null && !ValueUtil.isEmpty(unidade.flIgnoraMultEspecial)) return unidade.ignoraMultEspecial();
		if (tipoPedido != null && !ValueUtil.isEmpty(tipoPedido.flIgnoraMultEspecial)) return tipoPedido.isIgnoraMultEspecial();
		return false;
    }
    
    public void validadeConversaoUnidade(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (pedido.getTipoPedido() != null && !pedido.getTipoPedido().isConsisteConversaoUnidade()) return;
    	ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
    	boolean validandoMultiploEspecial = false;
    	boolean ignoraMultEspecial = validarIgnoraMultiploEspecial(itemPedido, pedido);
    	boolean usaConversaoMultiploEspecial = LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() && !ignoraMultEspecial;
    	boolean usaConversaoMultiploEspecialUnidadeAlternativa = usaConversaoMultiploEspecial && LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa() && LavenderePdaConfig.usaUnidadeAlternativa && (produtoUnidade != null && produtoUnidade.nuMultiploEspecial != 0) && !ignoraMultEspecial;
        if ((LavenderePdaConfig.usaConversaoUnidadesMedida && (LavenderePdaConfig.isConsisteConversaoUnidades() || (usaConversaoMultiploEspecial) || LavenderePdaConfig.avisaConversaoUnidades || LavenderePdaConfig.consisteMultiploEmbalagem))
        		|| (usaConversaoMultiploEspecial)) {
        	Cliente cli = SessionLavenderePda.getCliente();
        	Produto produtoItem = itemPedido.getProduto();
        	if (!LavenderePdaConfig.consisteConversaoUnidadesPorCliente || (!(ValueUtil.VALOR_NAO.equals(cli.flConsisteConversaoUnidade)))) {
	        	if (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade))) {
	        		double nuConversaoUnidade = 0;
					if (usaConversaoMultiploEspecialUnidadeAlternativa) {
	        			nuConversaoUnidade = produtoUnidade.nuMultiploEspecial;
	        		} else if (usaConversaoMultiploEspecial) {
	        			nuConversaoUnidade = produtoItem.nuMultiploEspecial;
	        		} else if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado && cli.isCreditoAntecipado()) {
	        			nuConversaoUnidade = produtoItem.nuConversaoUMCreditoAntecipado;
	        		} else if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.avisaConversaoUnidades) {
	        			nuConversaoUnidade = produtoItem.nuConversaoUnidadesMedida;
	        		}
	        		
	        		if (nuConversaoUnidade == 0) {
	        			nuConversaoUnidade = 1;
	        		}

			        if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido && LavenderePdaConfig.consisteConversaoUnidadesIgnoraEstoqueQtdDesejada()) {
				        if (isConversaoUnidadeCompleta(nuConversaoUnidade, itemPedido.qtItemDesejado)) return;
			        } else if (isConversaoUnidadeCompleta(nuConversaoUnidade, itemPedido.getQtItemFisico())) {
	        			//Consiste multiploEmbalagem
	        			consisteMultiploEmbalagem(itemPedido, produtoItem);
		        		//Conversão OK
	        			return;
	        		} else if (usaConversaoMultiploEspecial) {
	        			//Cconversão do múltiplo especial não foi OK, tenta do normal antes de gerar erro
	        			double nuConversaoUnidadeNormal = usaConversaoMultiploEspecialUnidadeAlternativa ? produtoUnidade.nuConversaoUnidade : produtoItem.nuConversaoUnidadesMedida;
		        		if (nuConversaoUnidadeNormal > 1) {
			        		if (isConversaoUnidadeCompleta(nuConversaoUnidadeNormal, itemPedido.getQtItemFisico())) {
			        			//Consiste multiploEmbalagem
			        			consisteMultiploEmbalagem(itemPedido, produtoItem);
				        		//Conversão OK
			        			return;
			        		}
		        		} else {
		        			validandoMultiploEspecial = true;
		        		}
		        		//else: significa que não há conversão normal, então deve reclamar da conversão especial.
	        		} else if (LavenderePdaConfig.consisteMultiploEmbalagem) {
						//Consiste multiploEmbalagem
						consisteMultiploEmbalagem(itemPedido, produtoItem);
						//Conversão OK
						return;
					}
	        		//--
        			if (itemPedido.flEditandoQtItemFaturamento && !validandoMultiploEspecial) {
    					String param1 = "";
    					String param2 = "";
        				if (usaConversaoMultiploEspecial) {
        					double vlConversao = 0.0;
        					double vlAnterior = 0.0;
        					if (produtoItem.nuMultiploEspecial != 0) {
        						vlConversao = produtoItem.nuConversaoUnidadesMedida / produtoItem.nuMultiploEspecial;
        						if (vlConversao != 0.0) {
        							vlConversao = 1 / vlConversao;
                					vlAnterior = itemPedido.qtItemFaturamento - (itemPedido.qtItemFaturamento % vlConversao); //Valor válido anterior ao valor digitado
        						}
        					}
        					param1 = StringUtil.getStringValueToInterface(vlAnterior);
        					param2 = StringUtil.getStringValueToInterface(vlAnterior + vlConversao);
        					if (param1.equals(StringUtil.getStringValue(0d))) {
        						if (vlConversao == 0.0) {
        							vlConversao = 1.0;
        						}
        						param1 = StringUtil.getStringValueToInterface(vlConversao);
        						param2 = StringUtil.getStringValueToInterface(2 * vlConversao);
        					}
        					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_CONVERSAO_UNIDADE_ITEM_FATUR_ESPEC, new Object[] {param1, param2}));
        				} else {
        					param1 = StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(itemPedido.qtItemFaturamento));
        					param2 = StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(itemPedido.qtItemFaturamento + 1));
        					if (param1.equals(StringUtil.getStringValue(0))) {
        						param1 = StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(itemPedido.qtItemFaturamento + 1));
        						param2 = StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(itemPedido.qtItemFaturamento + 2));
        					}
        					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_CONVERSAO_UNIDADE_ITEM_FATUR, new Object[] {param1, param2}));
        				}
        			}
        			//--
        			//Conversão com erro, prepara e emite mensagem ao usuário
        			int value = ValueUtil.doublePrecisionInterface;
        			if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
        				value = 0;
        			}
			        boolean usaQtdDesejada = LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido && LavenderePdaConfig.consisteConversaoUnidadesIgnoraEstoqueQtdDesejada();
        			
			        String param1 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade)), value);
			        String param2 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue((usaQtdDesejada ? itemPedido.qtItemDesejado : itemPedido.getQtItemFisico()) / nuConversaoUnidade, 0)) * nuConversaoUnidade, value);
			        String param3 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue((usaQtdDesejada ? itemPedido.qtItemDesejado : itemPedido.getQtItemFisico()) / nuConversaoUnidade, 0)) * nuConversaoUnidade + nuConversaoUnidade, value);
			        if (ValueUtil.getIntegerValue(param2) == 0) {
        				param2 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade)));
        				param3 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade + nuConversaoUnidade)));
        			}
    				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_CONVERSAO_UNIDADE, new Object[] { param1, param2, param3}));
	        	}
        	}
        }

    }

    private void consisteMultiploEmbalagem(ItemPedido itemPedido, Produto produtoItem) {
    	//Conversão da embalagem com base no nuMultiploEmbalagem do produto
    	if (LavenderePdaConfig.consisteMultiploEmbalagem) {
    		double vlMultiploEmbalagem = 1;
    		if (produtoItem.nuMultiploEmbalagem > 0) {
    			vlMultiploEmbalagem *= produtoItem.nuMultiploEmbalagem;
    		}
    		if (isConversaoUnidadeCompleta((long) vlMultiploEmbalagem, itemPedido.qtItemFaturamento)) {
    			//Conversão ok, qtItemFaturamento é multiplo do campo do produto
    			return;
    		} else {
    			if (produtoItem.nuMultiploEmbalagem == 0) {
    				throw new ValidationException(Messages.ITEMPEDIDO_MSG_SEM_MULTIPLO_EMBALAGEM);
    			} else {
    				double nuConversaoReal = produtoItem.nuMultiploEmbalagem;
    				//--
	    			String param1 = StringUtil.getStringValueToInterface(nuConversaoReal);
	    			String param2 = StringUtil.getStringValueToInterface(ValueUtil.getIntegerValueRoundUp(itemPedido.qtItemFaturamento / nuConversaoReal) * nuConversaoReal);
	    			String param3 = StringUtil.getStringValueToInterface((ValueUtil.getIntegerValueRoundUp(itemPedido.qtItemFaturamento / nuConversaoReal) + 1) * nuConversaoReal);
	    			if (param2.equals(StringUtil.getStringValue(0.0))) {
	    				param2 = StringUtil.getStringValueToInterface(nuConversaoReal);
	    				param3 = StringUtil.getStringValueToInterface(nuConversaoReal + nuConversaoReal);
	    			}
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_CONVERSAO_MULTIPLO_EMBALAGEM,
							new Object[] { param1, param2, param3}));
    			}
    		}
    	}
    }

    public double converteQtUnidadeFisicaToQtUnidadeFaturamento(Cliente cliente, Produto produtoRelacionado, double qtProdutoFaltante) {
    	double nuConversao;
    	if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado && cliente.isCreditoAntecipado()) {
    		nuConversao = produtoRelacionado.nuConversaoUMCreditoAntecipado;
    	} else {
    		nuConversao = produtoRelacionado.nuConversaoUnidadesMedida;
    	}
    	if (nuConversao == 0) {
    		nuConversao = 1;
    	}
    	return qtProdutoFaltante / nuConversao;
   	}
    
	public boolean isConversaoUnidadeCompleta(final double numeroConversao, final double qtItemVenda) {
		if (numeroConversao > 0) {
			double nuCasasDecimais = Math.pow(10, ValueUtil.doublePrecision);
			long nuConversaoLong = ValueUtil.getIntegerValue(numeroConversao * nuCasasDecimais);
			nuConversaoLong = nuConversaoLong > 0 ? nuConversaoLong : 1;
			long qtItem = ValueUtil.getIntegerValue(qtItemVenda * nuCasasDecimais);
			return qtItem % nuConversaoLong == 0;
		}
		return false;
	}

	public double arredondaObedecendoConversaoUnidade(final double numeroConversao, double qtItemVenda) {
		if (numeroConversao > 0) {
			long nuConversaoLong = ValueUtil.getIntegerValue(numeroConversao * 1000);
			long qtItem = ValueUtil.getIntegerValue(qtItemVenda * 1000);
			long result = (long) (qtItem % nuConversaoLong);
			while (result != 0) {
				qtItem++;
				result = (long) (qtItem % nuConversaoLong);
			}
			return ValueUtil.round(ValueUtil.round(qtItem) / 1000);
		}
		return 0;
	}
	
}