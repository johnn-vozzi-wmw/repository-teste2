package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.service.MenuCatalogoService;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.util.MediaUtil.MEDIA_ENTIDADE;
import totalcross.util.Vector;

public class SyncMediaManager {
	
	private LavendereWeb2Tc web2Tc;
	private LinkedHashMap<MEDIA_ENTIDADE, Integer> entidades;
	
	public SyncMediaManager(LavendereWeb2Tc web2Tc) {
		this.web2Tc = web2Tc;
		entidades = new LinkedHashMap<>();
	}
	
	public void verificaAtualizacaoMedia() throws SQLException {
		if (SessionLavenderePda.isDownloadingMedia()) {
			return;
		}
		verificaFotoProdutoPendente();
		verificaFotoClientePendente();
		verificaFotoDivulgaInfoPendente();
		verificaFotoProdutoGradePendente();
		verificaCatalogoExternoPendente();
		verificaVideoProdutoPendente();
		verificaVideoProdutoGradePendente();
		verificaFotoMenuCatalogoPendente();
		if (!entidades.isEmpty()) {
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.MEDIA_DOWNLOAD, Messages.CONFIRMACAO_RECEBIMENTO_MEDIA_SYNC) {
				@Override
				public void process() {
					SyncExtend.receiveFileForeGroung(entidades, message, web2Tc.paramsSync);
				}
			});
		}
	}
	
	private void verificaFotoProdutoPendente() throws SQLException {
		if (LavenderePdaConfig.isMostraFotoProduto()) {
			int count = 0;
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
				count = SyncExtend.getQtFotoProdutoEmpParaDownload();
				if (count > 0) {
					entidades.put(MEDIA_ENTIDADE.FOTOPRODUTOEMP, count);
				}
			} else {
				count = SyncExtend.getQtFotoProdutoParaDownload();
				if (count > 0) {
					entidades.put(MEDIA_ENTIDADE.FOTOPRODUTO, count);
				}
			}
		}
	}

	private void verificaFotoClientePendente() throws SQLException {
		if (LavenderePdaConfig.usaFotoCliente()) {
			int count = SyncExtend.getQtFotoClienteParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.FOTOCLIENTEERP, count);
			}
		}
	}
	
	private void verificaFotoDivulgaInfoPendente() throws SQLException {
		if (LavenderePdaConfig.usaDivulgaInformacao) {
			int count = SyncExtend.getQtFotoDivulgacaoInfoParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.DIVULGAINFO, count);
			}
		}
	}
	private void verificaFotoProdutoGradePendente() throws SQLException {
		if (LavenderePdaConfig.isUsaFotoProdutoGrade()) {
			int count = SyncExtend.getQtFotoProdutoGradeParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.FOTOPRODUTOGRADE, count);
			}
		}
	}

	private void verificaCatalogoExternoPendente() throws SQLException {
		if (LavenderePdaConfig.isUsaArquivoCatalogoExternoCapaPedido() || LavenderePdaConfig.isUsaArquivoCatalogoExternoListaProdutos()) {
			int count = SyncExtend.getQtCatalogoExternoParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.CATALOGOEXTERNO, count);
			}
		}
	}
	
	private void verificaVideoProdutoPendente() throws SQLException {
		if (LavenderePdaConfig.isUsaConfigVideosProdutos()) {
			int count = SyncExtend.getQtVideProdutoParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.VIDEOPRODUTO, count);
			}
		}
	}
	
	private void verificaVideoProdutoGradePendente() throws SQLException {
		if (LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			int count = SyncExtend.getQtVideProdutoGradeParaDownload();
			if (count > 0) {
				entidades.put(MEDIA_ENTIDADE.VIDEOPRODUTOGRADE, count);
			}
		}
	}
	
	private void verificaFotoMenuCatalogoPendente() throws SQLException {
		int count = 0;
		if (LavenderePdaConfig.isUsaConfigMenuCatalogo()) {
			Vector menuCatalogoList = MenuCatalogoService.getInstance().findAllEntidadesAgrupadas();
			int size = menuCatalogoList.size();
			for (int i = 0; i < size; i++) {
				MenuCatalogo menuCatalogo = (MenuCatalogo) menuCatalogoList.elementAt(i);
				count += web2Tc.possuiAtualizacaoMenuCatalogo(web2Tc.getAtualizacaoMenuCatalogo(menuCatalogo));
			}
		}
		if (count > 0) {
			entidades.put(MEDIA_ENTIDADE.MENUCATALOGO, count);
		}
	}

}
