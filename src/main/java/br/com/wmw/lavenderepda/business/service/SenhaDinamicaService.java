package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.BaseService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.Random;
import totalcross.util.Vector;

public class SenhaDinamicaService extends BaseService {

	private static Random r = new Random();

	public static boolean forceString;

	private static String[] fatores;

	private static int getRadomValue(int index) throws SQLException {
		int intervaloTempo = LavenderePdaConfig.usaNumerosAleatoriosSalvosGeracaoSenha;
		if (intervaloTempo > 0) {
			if (validatePersistenteFatores(intervaloTempo) && fatores[index] != null) {
				return ValueUtil.getIntegerValue(fatores[index]);
			}
			ConfigInternoService.getInstance().addValue(ConfigInterno.DATAHORAULTIMAGERACAOALEATORIOS,
					TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS());
			int randomValue = r.between(1, 9999);
			if (index == 1) {
				String ultimoNumGerado = ConfigInternoService.getInstance()
						.getVlConfigInterno(ConfigInterno.ULTIMOSNUMEROSALEATORIOSGERADOS);
				if (ultimoNumGerado != null) {
					ConfigInternoService.getInstance().addValue(ConfigInterno.ULTIMOSNUMEROSALEATORIOSGERADOS,
							ultimoNumGerado + "," + randomValue);
				}
			} else {
				ConfigInternoService.getInstance().addValue(ConfigInterno.ULTIMOSNUMEROSALEATORIOSGERADOS,
						StringUtil.getStringValue(randomValue));
			}
			return randomValue;
		}
		return r.between(1, 9999);
	}

	public static int getFator1() throws SQLException {
		return getRadomValue(0);
	}

	public static int getFator2() throws SQLException {
		return getRadomValue(1);
	}

	public static void validateSenhaMultiplasLiberacoes(int fator1, int fator2, String cdCliente, int senhaDigitada,
			Vector chaveSementeList) {
		if (ValueUtil.isNotEmpty(chaveSementeList)) {
			int senhaCorreta = 0;
			for (int i = 0; i < chaveSementeList.size(); i++) {
				int chaveSemente = (int) chaveSementeList.items[i];
				forceString = true;
				switch (chaveSemente) {
				case SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA:
					if (LavenderePdaConfig.sementeSenhaTipoPedido > 0) {
						senhaCorreta += getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaTipoPedido,
								cdCliente);
					} else {
						throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
					}
					break;
				case SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO:
					if (LavenderePdaConfig.sementeSenhaSaldoBonificacaoExtrapolado > 0) {
						senhaCorreta += getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaSaldoBonificacaoExtrapolado, cdCliente);
					} else {
						throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
					}
					break;
				case SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO:
					if (LavenderePdaConfig.sementeSenhaClienteAtrasadoNovoPedido > 0) {
						senhaCorreta += getSenhaCorreta(fator1, fator2,
								LavenderePdaConfig.sementeSenhaClienteAtrasadoNovoPedido, cdCliente);
					} else {
						throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
					}
					break;
				case SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP:
					if (LavenderePdaConfig.sementeSenhaLimiteCreditoCliente > 0) {
						senhaCorreta += getSenhaCorreta(fator1, fator2,
								LavenderePdaConfig.sementeSenhaLimiteCreditoCliente, cdCliente);
					} else {
						throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
					}
					break;
				case SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA:
					if (LavenderePdaConfig.sementeSenhaVisitaClienteForaAgenda > 0) {
						senhaCorreta += getSenhaCorreta(fator1, fator2,
								LavenderePdaConfig.sementeSenhaVisitaClienteForaAgenda, cdCliente);
					} else {
						throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
					}
					break;
				}
			}
			senhaCorreta = senhaCorreta / chaveSementeList.size();
			if (senhaDigitada != senhaCorreta) {
				throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
			}
		}
	}

	public static void validateSenha(int fator1, int fator2, String cdCliente, String cdProduto, double qtItem,
			double vlItem, int senhaDigitada, int chaveSemente, String vlTotalPedido, String cdGrupoProduto1,
			String cdGrupoProduto2, String cdGrupoProduto3, double qtPesoExcedente, String cdProdutoRelacionado,
			String cdUsuario, String nuCnpj, Date data, String cdCondPagto, String tempoLiberacao) throws SQLException {
		forceString = true;
		switch (chaveSemente) {
		case SenhaDinamica.SENHA_LIBERACAO_DESCONTOMAXIMOITEM:
			if (LavenderePdaConfig.sementeSenhaDescontoMaximoPedidoPendente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaDescontoMaximoPedidoPendente, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_VENDA_PRODUTO_SEM_ESTOQUE:
			if (LavenderePdaConfig.sementeSenhaVendaProdutoSemEstoque > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, qtItem,
						LavenderePdaConfig.sementeSenhaVendaProdutoSemEstoque, cdProduto, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_ATUALIZACAO_AGENDA_VISITA:
			if (LavenderePdaConfig.sementeSenhaConfirmacaoSubstituicaoCadastroAgendaVisita > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaConfirmacaoSubstituicaoCadastroAgendaVisita, nuCnpj);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CAD_COORDENADAS:
			if (LavenderePdaConfig.sementeSenhaCancelamentoCadCoordenadas > 0) {
				if (ValueUtil.isEmpty(cdCliente)) {
					cdCliente = nuCnpj;
				}
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaCancelamentoCadCoordenadas, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_PRODUTO_RELACIONADO:
			if (LavenderePdaConfig.sementeSenhaVendaRelacionada > 0) {
				int senhaCorreta = getSenha(fator1, fator2, LavenderePdaConfig.sementeSenhaVendaRelacionada,
						cdProdutoRelacionado);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO:
			if (LavenderePdaConfig.sementeSenhaClienteAtrasadoNovoPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaClienteAtrasadoNovoPedido, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP:
			if (LavenderePdaConfig.sementeSenhaLimiteCreditoCliente > 0) {
				int senhaCorreta = 0;
				if (ValueUtil.isNotEmpty(vlTotalPedido) && ValueUtil.isNotEmpty(cdUsuario)) {
					String vlTotalPedidoString = ValueUtil.isNotEmpty(vlTotalPedido) ? vlTotalPedido : "1";
					String cdUsuarioValue = ValueUtil.isNotEmpty(cdUsuario) ? cdUsuario : "1";
					senhaCorreta = getSenhaCorreta(fator1, LavenderePdaConfig.sementeSenhaLimiteCreditoCliente, cdCliente, vlTotalPedidoString, cdUsuarioValue);
				} else if (ValueUtil.isNotEmpty(vlTotalPedido) && !ValueUtil.valueEquals(LavenderePdaConfig.cdStatusPedidoConsignado, ValueUtil.VALOR_NAO)
							|| LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
					String vlTotalPedidoString = ValueUtil.isNotEmpty(vlTotalPedido) ? vlTotalPedido : "1";
					senhaCorreta = getSenhaCorreta(fator1, LavenderePdaConfig.sementeSenhaLimiteCreditoCliente, cdCliente, vlTotalPedidoString, "1");
				} else {
					senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaLimiteCreditoCliente, cdCliente);
				}
				senhaCorreta = senhaCorreta >= 0 ? senhaCorreta : senhaCorreta * -1;
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_PRODUTO_BLOQUEADO_VENDA:
			if (LavenderePdaConfig.sementeSenhaLiberaComSenhaVendaProdutoBloqueado > 0) {
				int senhaCorreta = 0;
				senhaCorreta = getSenhaCorreta(fator1,
						LavenderePdaConfig.sementeSenhaLiberaComSenhaVendaProdutoBloqueado, cdGrupoProduto1,
						cdGrupoProduto2, cdGrupoProduto3, qtPesoExcedente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_PRECO_VENDA:
			if (LavenderePdaConfig.sementeSenhaPrecoDeVenda > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaPrecoDeVenda,
						cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA:
			if (LavenderePdaConfig.sementeSenhaTipoPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaTipoPedido,
						cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_NOVO_PEDIDO_SEMENVIAR_DADOS:
			if (LavenderePdaConfig.sementeSenhaObrigaEnvioDePedidos > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaObrigaEnvioDePedidos,
						cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_NOVO_PEDIDO_SEMRECEBER_DADOS:
			if (LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaNovoPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaNovoPedido, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_SISTEMA_SEMRECEBER_DADOS:
			if (LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaUsoSistema > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaUsoSistema, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_SISTEMA_ENVIAR_PEDIDO_BYHORA:
			if (LavenderePdaConfig.sementeSenhaHoraLimiteEnvioPedidos > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaHoraLimiteEnvioPedidos, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_BONIFICACAO_PEDIDO:
			if (LavenderePdaConfig.sementeSenhaBonificarProdutoPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaBonificarProdutoPedido, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_PRECO_PRODUTO:
			if (LavenderePdaConfig.sementeSenhaLiberaPrecoProduto > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaLiberaPrecoProduto,
						cdCliente, cdProduto, qtItem, vlItem);
				String senhaStr = StringUtil.getStringValue(senhaCorreta);
				if (senhaStr.length() > 6) {
					senhaStr = senhaStr.substring(senhaStr.length() - 6);
				}
				if (senhaDigitada != ValueUtil.getIntegerValue(senhaStr)) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CLIENTE_SEM_PEDIDO:
			if (LavenderePdaConfig.sementeSenhaClienteSemPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaClienteSemPedido,
						cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_CLIENTE_REDE_ATRASADO_NOVO_PEDIDO:
			if (LavenderePdaConfig.sementeSenhaClienteRedeAtrasadoNovoPedido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaClienteRedeAtrasadoNovoPedido, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_DESBLOQUEAR_LIMITADOR:
			if (LavenderePdaConfig.liberaSenhaQuantidadeMaximaVendaProduto && LavenderePdaConfig.sementeSenhaLiberacaoQtMaxVendaPorProduto > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaLiberacaoQtMaxVendaPorProduto, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_SISTEMA_BLOQUEADO_GPS_INATIVO:
			if (LavenderePdaConfig.sementeSenhaBloqueiaSistemaGpsInativo > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaBloqueiaSistemaGpsInativo, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_SUGESTADO_VENDA:
			if (LavenderePdaConfig.sementeSenhaSugestaoVendaObrigatoria > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaSugestaoVendaObrigatoria, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_SQL_EXECUTOR:
			if (LavenderePdaConfig.sementeSenhaSqlExecutor > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaSqlExecutor,
						cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				} else {
					LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO,
							LogPda.LOG_CATEGORIA_LIBERACAO_SENHA_SQL_EXECUTOR,
							Messages.LOGPDA_MSG_LIBERACAO_SENHA_SQL_EXECUTOR);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_VALIDADE_CARGA:
			if (LavenderePdaConfig.sementeSenhaFecharCargaVencida > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaFecharCargaVencida,
						cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_PESO_MINIMO_CARGA:
			if (LavenderePdaConfig.sementeSenhaFecharCargaPesoMenorMinimo > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaFecharCargaPesoMenorMinimo, cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_BASEADO_PERCENTUAL_USUARIO_ESCOLHIDO:
			if (LavenderePdaConfig.sementeSenhaPrecoBaseadoPercentualUsuarioEscolhido > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaPrecoBaseadoPercentualUsuarioEscolhido, cdCliente, cdProduto,
						cdUsuario, qtItem, vlItem);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_QTD_ITEM_BONIFICACAO:
			if (LavenderePdaConfig.sementeSenhaQtdItemMaiorPedidoOriginal > 0) {
				int senhaCorreta = getSenhaProduto(fator1, fator2,
						LavenderePdaConfig.sementeSenhaQtdItemMaiorPedidoOriginal, cdProduto);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA:
			if (LavenderePdaConfig.sementeSenhaVisitaClienteForaAgenda > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2,
						LavenderePdaConfig.sementeSenhaVisitaClienteForaAgenda, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_DIA_ENTREGA_PEDIDO:
		case SenhaDinamica.SENHA_LIBERACAO_FECHAMENTO_DIARIO:
			int semente = ValueUtil.valueEquals(SenhaDinamica.SENHA_LIBERACAO_DIA_ENTREGA_PEDIDO, chaveSemente) ? LavenderePdaConfig.sementeSenhaDiaEntregaPedido : LavenderePdaConfig.sementeSenhaFechamentoDiario;
			if (semente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, semente, data, cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_MENU_ADMINISTRACAO_TABELAS:
			if (LavenderePdaConfig.sementeSenhaAdministracaoTabelas > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaAdministracaoTabelas,
						cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_TOLERANCIA_VERBA_POSITIVA:
			if (LavenderePdaConfig.sementeSenhaValorAbaixoMinimoVerba > 0) {
				int senhaCorreta = 0;
				if (ValueUtil.isEmpty(vlTotalPedido)) {
					senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaValorAbaixoMinimoVerba, cdCliente);
				} else {
					senhaCorreta = getSenhaCorreta(fator1, LavenderePdaConfig.sementeSenhaValorAbaixoMinimoVerba, cdCliente, vlTotalPedido, "1");
				}
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CANCELAMENTO_CONSIGNACAO:
			if (LavenderePdaConfig.sementeSenhaLiberaComSenhaCancelamentoConsignacao > 0) {
				int senhaCorreta = 0;
				senhaCorreta = getSenhaCorreta(fator1, LavenderePdaConfig.sementeSenhaLiberaComSenhaCancelamentoConsignacao, cdCliente, vlTotalPedido, "1");
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO:
			if (LavenderePdaConfig.sementeSenhaSaldoBonificacaoExtrapolado > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.sementeSenhaSaldoBonificacaoExtrapolado, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_BONIFICACAO_EXTRAPOLADO:
			int vlSemente = LavenderePdaConfig.sementeSenhaPedidoBonificacaoComSaldoVerbaExtrapolado;
			if (vlSemente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, vlSemente, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_CONTROLE_DATAHORA_ACESSO_SISTEMA:
			ValorParametro parametro = LavenderePdaConfig.getParametroPorSistema(ValorParametro.SEMENTESENHACONTROLEDATAHORANOACESSOAOSISTEMA);
			if (parametro.vlParametro.compareTo("0") > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, Integer.valueOf(parametro.vlParametro), cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_CONTROLE_VENCIMENTO_CERTIFICADO_IOS:
			semente = LavenderePdaConfig.sementeSenhaControleVencimentoCertificadoIos;
			if (semente > 0) {
				int senha = getSenhaCorreta(fator1, fator2, semente, getValueGeracaoSenha(cdUsuario));
				if (senhaDigitada != senha) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_EXTRAPOLADO:
			semente = LavenderePdaConfig.sementeSenhaPedidoComSaldoVerbaExtrapolado;
			if (semente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, semente, cdCliente, vlTotalPedido, "1");
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_VALOR_MINIMO_PARCELA:
			semente = LavenderePdaConfig.getSementeSenhaVlMinParcela();
			if (semente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, semente, cdCliente, vlTotalPedido, "1");
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_RESTAURACAO_BACKUP:
			semente = LavenderePdaConfig.getSementeSenhaLiberaComSenhaRestauracaoBackup();
			if (semente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, fator2, semente, cdUsuario);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CONDICAO_PAGAMENTO_POR_DIAS_PERMITIDO_CLIENTE:
			semente = LavenderePdaConfig.getSementeSenhaCondPagamento();
			if (semente > 0) {
				int senhaCorreta = getSenhaCorreta(fator1, semente, cdCliente, vlTotalPedido, cdCondPagto);
				senhaCorreta = senhaCorreta >= 0 ? senhaCorreta : senhaCorreta*-1;
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CAD_COORDENADAS_PESQUISA_MERCADO:
			if (LavenderePdaConfig.getSementeSenhaCadastroCoordenadaNaPesquisaDeMercado() > 0) {
				if (ValueUtil.isEmpty(cdCliente)) {
					cdCliente = nuCnpj;
				}
				int senhaCorreta = getSenhaCorreta(fator1, fator2, LavenderePdaConfig.getSementeSenhaCadastroCoordenadaNaPesquisaDeMercado(), cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_CAD_FOTOS_PESQUISA_MERCADO:
			semente = LavenderePdaConfig.getSementeSenhaCadastroFotoProdutoNaPesquisaDeMercado();
			if (semente > 0) {
				if (ValueUtil.isEmpty(cdCliente)) {
					cdCliente = nuCnpj;
				}
				int senhaCorreta = getSenhaCorreta(fator1, fator2, semente, cdCliente);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_LIBERACAO_RELACIONAR_PEDIDO_PRODUCAO:
			semente = LavenderePdaConfig.getSementeSenhaPedidoProducao();
			if (semente > 0) {
				int senhaCorreta = getSenha(fator1, fator2, semente, "");
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		case SenhaDinamica.SENHA_SISTEMA_BLOQUEADO:
			semente = LavenderePdaConfig.sementeSenhaLiberacaoSisBloqueado;
			if (semente > 0) {
				int senhaCorreta = getSenha(fator1, fator2, semente, cdUsuario, tempoLiberacao);
				if (senhaDigitada != senhaCorreta) {
					throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
				}
			} else {
				throw new ValidationException(Messages.MSG_SEMENTE_SENHADINAMICA_INVALIDA);
			}
			break;
		}
	}

	public static int getSenha(int fator1, int fator2, int semente, String cdUsuario, String tempoLiberacao) {
		int tempoLiberacaoSegundos = getTempoLiberacaoSegundos(tempoLiberacao);
		int cdUsuarioInt = getValueGeracaoSenha(cdUsuario);
		return (((fator1 * fator2) / semente) + semente + cdUsuarioInt) + (tempoLiberacaoSegundos / 60);
	}

	private static int getTempoLiberacaoSegundos(String tempoLiberacao) {
		if (TimeUtil.isValidTimeHHMM(tempoLiberacao)) {
			int seconds = TimeUtil.getSecondsBetween(tempoLiberacao + ":00", "00:00:00");
			if (seconds < 600) {
				throw new ValidationException(Messages.MSG_TEMPO_MINIMO_LIBERACAO_ACESSO);
			}
			return seconds;
		} else {
			throw new ValidationException(Messages.MSG_VALOR_INVALIDO_LIBERACAO_ACESSO);
		}
	}

	private static int getSenhaCorreta(int fator1, double qtItem, int semente, String cdProduto, String cdCliente) {
		int cdClienteInt = getValueGeracaoSenha(cdCliente);
		int cdProdutoInt = getValueGeracaoSenha(cdProduto);
		return (((fator1 * ValueUtil.getIntegerValueTruncated(qtItem)) / semente) + semente + cdProdutoInt + cdClienteInt) / 2;
	}

	public static int getSenhaCorreta(int fator1, int fator2, int semente, String cdClienteString) {
		return getSenhaCorreta(fator1, fator2, semente, cdClienteString, "", 0, 0);
	}

	public static int getSenhaCorreta(int fator1, int fator2, int semente, String cdClienteString, String cdProduto, double qtdItem, double vlItem) {
		return getSenhaCorreta(fator1, fator2, semente, cdClienteString, cdProduto, qtdItem, vlItem, "");
	}

	public static int getSenhaCorreta(int fator1, int fator2, int semente, String cdClienteString, String cdProduto,
			String cdUsuario, double qtdItem, double vlItem) {
		return getSenhaCorreta(fator1, fator2, semente, cdClienteString, cdProduto, cdUsuario, qtdItem, vlItem, "");
	}

	public static int getSenhaCorreta(int fator1, int fator2, int semente, String cdClienteString, String cdProduto,
			String cdUsuario, double qtdItem, double vlItem, String vlTotalPedido) {
		int cdCliente = getValueGeracaoSenha(cdClienteString);
		int cdProdutoInt = getValueGeracaoSenha(cdProduto);
		int cdUsuarioInt = getValueGeracaoSenha(cdUsuario);
		int resultMult = ValueUtil.getIntegerValueTruncated(qtdItem * vlItem);
		String result = StringUtil.getStringValue(resultMult);
		if (result.length() > 4) {
			resultMult = ValueUtil.getIntegerValue(result.substring(0, 4));
		}
		int senhaGerada = ((fator1 * resultMult * semente) / cdUsuarioInt) + ((fator2 * resultMult * cdUsuarioInt) / semente) + semente + cdCliente + cdProdutoInt;
		return senhaGerada > 0 ? senhaGerada : senhaGerada * -1;

	}

	public static int getSenhaCorreta(int fator1, int fator2, int semente, String cdClienteString, String cdProduto, double qtdItem, double vlItem, String vlTotalPedido) {
		int cdCliente = getValueGeracaoSenha(cdClienteString);
		int cdProdutoInt = getValueGeracaoSenha(cdProduto);

		int resultMult = ValueUtil.isNotEmpty(cdProduto) ? ((Double)(qtdItem * vlItem)).intValue() : fator2;
		String result = StringUtil.getStringValue(resultMult);

		if (result.length() > 4) {
			resultMult = ValueUtil.getIntegerValue(result.substring(0, 4));
		}

		int senhaInt;
		if( ValueUtil.isNotEmpty(cdProduto)){
			Double senhaD = ((((fator1 * resultMult) / semente) + semente) * vlItem + cdCliente + cdProdutoInt) / 2;
			senhaInt = senhaD.intValue();
		} else {
			senhaInt = ((((fator1 * resultMult) / semente) + semente) + cdCliente + cdProdutoInt) / 2;
		}
		return senhaInt;
	}

	public static int getSenhaCorreta(int fator1, int semente, String cdClienteString, String vlTotalPedido, String cdUsuarioString) {
		int cdCliente = getValueGeracaoSenha(cdClienteString);
		int cdUsuario = getValueGeracaoSenha(cdUsuarioString);
		int vlPedido = ValueUtil.getIntegerValue(ValueUtil.getDoubleValue(vlTotalPedido));
		int vlTotalPedidoInt = getValueGeracaoSenha(StringUtil.getStringValue(vlPedido));
		int result = 0;
		if (vlTotalPedidoInt != 0) {
			result = vlTotalPedidoInt;
		}
		if (StringUtil.getStringValue(result).toString().length() > 4) {
			result = ValueUtil.getIntegerValue(StringUtil.getStringValue(result).substring(0, 4));
		}
		return (((fator1 * result * cdUsuario) / semente) + semente + cdCliente) / 2;
	}
	
	public static int getSenhaCorreta(int fator1, int fator2, int semente, Date data, String cdUsuario) {
		int nuDia = data.getDay();
		int nuMes = data.getMonth();
		int nuAno = data.getYear();
		int cdUsuarioInt = LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario() ? getValueGeracaoSenha(cdUsuario) : 0;
		return ((fator1 * fator2 * nuDia) / semente) + semente + cdUsuarioInt + (nuMes * nuAno) / 2;
	}

	public static int getSenhaCorreta(int fator1, int semente, String cdGrupoProduto1, String cdGrupoProduto2,
			String cdGrupoProduto3, double qtPesoExcedente) {
		int vlSumGruposTotal = ValueUtil.getIntegerValue(ValueUtil.getDoubleValue(cdGrupoProduto1)) + ValueUtil.getIntegerValue(ValueUtil.getDoubleValue(cdGrupoProduto2)) + ValueUtil.getIntegerValue(ValueUtil.getDoubleValue(cdGrupoProduto3));
		int vlSumGruposGeracao = getValueGeracaoSenha(StringUtil.getStringValue(vlSumGruposTotal));
		int qtPeso = ValueUtil.getIntegerValue(qtPesoExcedente);
		int qtPesoInt = getValueGeracaoSenha(StringUtil.getStringValue(qtPeso));
		int result = 0;
		if (qtPesoInt != 0) {
			result = qtPesoInt;
		}
		return (((fator1 * result) / semente) + semente + vlSumGruposGeracao) / 2;
	}

	public static int getSenhaProduto(int fator1, int fator2, int semente, String cdProduto) {
		return getSenha(fator1, fator2, semente, cdProduto);
	}

	private static int getSenha(int fator1, int fator2, int semente, String cdProdutoRelacionado) {
		int cdProduto = getValueGeracaoSenha(cdProdutoRelacionado);
		return (((fator1 * fator2) / semente) + semente + cdProduto) / 2;
	}

	protected static int getSenhaCorreta(int fator1, int fator2, int semente, int cdUsuario) {
		if (fator1 == 0 || fator2 == 0 || semente == 0) {
			return 0;
		}
		return (((fator1 * fator2) / semente) + semente + cdUsuario) / 2;
	}

	public static int getValueGeracaoSenha(String value) {
		if (ValueUtil.isEmpty(value)) {
			return 0;
		}
		if (value.length() > 5) {
			value = value.substring(value.length() - 5, value.length());
		}
		int returnValue = 0;
		try {
			if (!forceString) {
				returnValue = ValueUtil.getIntegerValue(value);
			} else {
				returnValue = Convert.toInt(value);
			}
		} catch (InvalidNumberException ine) {
			String valueFinal = "";
			for (char carcater : value.toCharArray()) {
				valueFinal += (int) carcater;
			}
			try {
				long numero = Convert
						.toLong(valueFinal.substring(0, valueFinal.length() > 10 ? 10 : valueFinal.length()));
				returnValue = (int) (numero / 15);
			} catch (InvalidNumberException nfex) {
				returnValue = 0;
			}
		}
		if (returnValue < 0) {
			returnValue = 0;
		}
		return returnValue;
	}

	private static boolean validatePersistenteFatores(int qtTempo) throws SQLException {
		String timeUltimaGeracao = ConfigInternoService.getInstance()
				.getVlConfigInterno(ConfigInterno.DATAHORAULTIMAGERACAOALEATORIOS);
		if (timeUltimaGeracao != null) {
			Time dtHoraUltimaGeracao = TimeUtil.getTime(timeUltimaGeracao);
			Time dtHoraAtual = TimeUtil.getCurrentTime();
			if (TimeUtil.getSecondsRealBetween(dtHoraAtual, dtHoraUltimaGeracao) / 60 < qtTempo) {
				fatores = StringUtil.splitTurbo(ConfigInternoService.getInstance()
						.getVlConfigInterno(ConfigInterno.ULTIMOSNUMEROSALEATORIOSGERADOS), ',', 2);
				return true;
			}
		}
		fatores = null;
		return false;
	}

}
