package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CatalogoParams;
import br.com.wmw.lavenderepda.business.domain.dto.CatalogoProdutoDTO;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;

public class CatalogoProdutoService {

	private static CatalogoProdutoService instance = null;
	public static boolean downloading;

	private CatalogoProdutoService() {
	}

	public static CatalogoProdutoService getInstance() {
		if (instance == null) {
			instance = new CatalogoProdutoService();
		}
		return instance;
	}

	public void gerarCatalogo(JSONObject catalogoParams) {
		try {
			LogSync.logSection(Messages.GERAR_CATALOGO);
			LogSyncTimer syncTimer = new LogSyncTimer("Início geração catálogo");
			CatalogoProdutoService.downloading = true;
			try {
				SyncManager.geraCatalogoProduto(catalogoParams);
			} finally {
				syncTimer.finish();
				LogSync.logSection("Fim geração catálogo");
				CatalogoProdutoService.downloading = false;
			}
		} catch (Throwable e) {
			String message = MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_CATALOGO_PRODUTO, StringUtil.clearEnterException(e.getMessage()));
			throw new ValidationException(message);
		}		
	}
	
	public JSONObject getCatalogoProdutoJson(CatalogoParams catalogo) {
		CatalogoProdutoDTO catProdDTO = new CatalogoProdutoDTO(catalogo);
		return new JSONObject(catProdDTO);
	}

}
