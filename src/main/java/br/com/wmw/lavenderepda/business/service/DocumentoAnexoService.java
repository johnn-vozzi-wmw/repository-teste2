package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DocumentoAnexoDbxDao;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sql.Types;
import totalcross.util.Date;
import totalcross.util.Vector;

public class DocumentoAnexoService extends CrudService {

    private static DocumentoAnexoService instance = null;
    
    private DocumentoAnexoService() {
        //--
    }
    
    public static DocumentoAnexoService getInstance() {
        if (instance == null) {
            instance = new DocumentoAnexoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DocumentoAnexoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public DocumentoAnexo anexarDocumento(String nmEntidade, String dsChave, String dsPath, String nmArquivo, boolean setDadosAlteracao, boolean insertFile) throws SQLException {
		validadeFile(dsPath);
		DocumentoAnexo documentoAnexo = new DocumentoAnexo();
		documentoAnexo.cdDocumentoAnexo = ValueUtil.getIntegerValue(generateIdGlobal());
		//--
		String dsCaminhoDestino = DocumentoAnexo.getPathDoc();
		String dsFilePath = dsCaminhoDestino + "/" + documentoAnexo.cdDocumentoAnexo + nmArquivo;
		dsFilePath = dsFilePath.replace("\\", "/");
		//--		
		documentoAnexo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (nmEntidade.equals(DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE) && SessionLavenderePda.isUsuarioSupervisor()) {
			String[] vlChave = dsChave.split(";");
			documentoAnexo.cdRepresentante = vlChave[1];
		} else {
			documentoAnexo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		documentoAnexo.nmEntidade = nmEntidade;
		documentoAnexo.nmArquivo = nmArquivo;
		documentoAnexo.dsChave = dsChave;
		documentoAnexo.dtDocumento = DateUtil.getCurrentDate();
		documentoAnexo.baArquivo = documentoAnexo.cdDocumentoAnexo + nmArquivo;
		documentoAnexo.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		documentoAnexo.dsPath = dsPath;
		if (!DocumentoAnexo.NM_ENTIDADE_PEDIDO.equals(nmEntidade) || setDadosAlteracao) {
			documentoAnexo.flTipoAlteracao = DocumentoAnexo.FLTIPOALTERACAO_INSERIDO;
		}
		if (insertFile) {
			getCrudDao().insert(documentoAnexo);
			try {
				FileUtil.createDirIfNecessary(dsCaminhoDestino);
				FileUtil.copyFile(dsPath, dsFilePath);
			} catch (IOException e) {
				UiUtil.showErrorMessage(e);
			}
		}
		return documentoAnexo;
	}

	private void validadeFile(String dsFilePath) {
		int limiteMegabytes = LavenderePdaConfig.limiteMegabytes;
		if (limiteMegabytes > 0) {
			if (FileUtil.exists(dsFilePath)) {
				File file = null;
				try {
					file = new File(dsFilePath, File.READ_ONLY);
					if (file.getSize() / FileUtil.INDEX_BITS_TO_MEGABYTES > limiteMegabytes) {
						throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_LIMITEMEGABYTES, limiteMegabytes));
					}
				} catch (ValidationException e) {
					throw e;
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				} finally {
					if (file != null) {
						FileUtil.closeFile(file);
					}
				}
			}
		}
	}

	public void insertDocumentoAnexoList(Pedido pedido) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.docAnexoList)) {
			for (int i = 0; i < pedido.docAnexoList.size(); i++) {
				DocumentoAnexo documentoAnexo = (DocumentoAnexo) pedido.docAnexoList.items[i];
				documentoAnexo.dsChave = pedido.getRowKey();
				getCrudDao().insert(documentoAnexo);
				try {
					String dsCaminhoDestino = DocumentoAnexo.getPathDoc();
					String dsFilePath = dsCaminhoDestino + "/" + documentoAnexo.cdDocumentoAnexo + StringUtil.getStringValue(documentoAnexo.nmArquivo);
					dsFilePath = dsFilePath.replace("\\", "/");
					//--
					FileUtil.createDirIfNecessary(dsCaminhoDestino);
					FileUtil.copyFile(StringUtil.getStringValue(documentoAnexo.dsPath), dsFilePath);
				} catch (IOException e) {
					UiUtil.showErrorMessage(e);
				}
			}
		}
		pedido.docAnexoList = new Vector();
	}

	public Vector getDocAnexoListToSync() throws SQLException {
		Vector docList = new Vector();
		Vector docAnexoList = DocumentoAnexoDbxDao.getInstance().findAllDocAnexoToSend();
		for (int i = 0; i < docAnexoList.size(); i++) {
			DocumentoAnexo documentoAnexo = (DocumentoAnexo) docAnexoList.items[i];
			docList.addElement(documentoAnexo.baArquivo);
		}
		return docList;
	}

	public void deleteByRowKey(String rowKey) throws SQLException {
		DocumentoAnexo documentoAnexo = (DocumentoAnexo) findByRowKey(rowKey);
		String fileName = DocumentoAnexo.getPathDoc() + StringUtil.getStringValue(documentoAnexo.baArquivo);
		FileUtil.deleteFile(fileName);
		delete(documentoAnexo);
	}
	
	public void deleteDocAnexo(String nmEntidade, String dsChave) throws SQLException {
		DocumentoAnexo documentoAnexo = new DocumentoAnexo();
		documentoAnexo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		documentoAnexo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		documentoAnexo.nmEntidade = nmEntidade;
		documentoAnexo.dsChave = dsChave;
		Vector docAnexoList = findAllByExample(documentoAnexo);
		int size = docAnexoList.size();
		for (int i = 0; i < size; i++) {
			DocumentoAnexo docAnexo = (DocumentoAnexo) docAnexoList.items[i];
			String fileName = DocumentoAnexo.getPathDoc() + StringUtil.getStringValue(docAnexo.baArquivo);
    		FileUtil.deleteFile(fileName);
		}
		deleteAllByExample(documentoAnexo);
	}

	public void atualizaDocAnexoParaEnvio(String nmEntidade, String dsChave, String flTipoAlteracao) throws SQLException {
		DocumentoAnexo documentoAnexo = new DocumentoAnexo();
		documentoAnexo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		documentoAnexo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		documentoAnexo.nmEntidade = nmEntidade;
		documentoAnexo.dsChave = dsChave;
		Vector docAnexoList = findAllByExample(documentoAnexo);
		int size = docAnexoList.size();
		for (int i = 0; i < size; i++) {
			DocumentoAnexo docAnexo = (DocumentoAnexo) docAnexoList.items[i];
			updateColumn(docAnexo.getRowKey(), DocumentoAnexo.NM_COLUNA_FLTIPOALTERACAO, flTipoAlteracao, Types.VARCHAR);
		}
	}

	public void deleteDocsAntigos() throws SQLException {
		int nuDias = LavenderePdaConfig.getNuDiasPermanenciaDocumentoAnexo();
		if (nuDias > 0) {
			VmUtil.debug("excluindo anexos");
			DocumentoAnexo documentoAnexoFilter = new DocumentoAnexo();
			Date dtLimite = new Date(); 
			DateUtil.decDay(dtLimite, nuDias);
			documentoAnexoFilter.dtDocumento = dtLimite;
			Vector docAnexoList = findAllByExample(documentoAnexoFilter);
			int size = docAnexoList.size();
			for (int i = 0; i < size; i++) {
				DocumentoAnexo documentoAnexo = (DocumentoAnexo) docAnexoList.items[i];
				deleteByRowKey(documentoAnexo.getRowKey());
			}
		}
	}
	
	public DocumentoAnexo anexarFotoDocumento(DocumentoAnexo documentoAnexo) throws SQLException {
		DocumentoAnexo docAnexo = documentoAnexo;
		docAnexo.dtDocumento = DateUtil.getCurrentDate();
		docAnexo.baArquivo = docAnexo.nmArquivo;
		docAnexo.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		docAnexo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (docAnexo.nmEntidade.equalsIgnoreCase(DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE) && SessionLavenderePda.isUsuarioSupervisor()) {
			String[] vlChave = docAnexo.dsChave.split(";");
			documentoAnexo.cdRepresentante = vlChave[1];
		} else {
			documentoAnexo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		if (!DocumentoAnexo.NM_ENTIDADE_PEDIDO.equalsIgnoreCase(docAnexo.nmEntidade)) {
			docAnexo.flTipoAlteracao = DocumentoAnexo.FLTIPOALTERACAO_INSERIDO;
		}
		getCrudDao().insert(documentoAnexo);

		return docAnexo;
	}
	
	public boolean hasAnexoNoPedido(String nmEntidade, String dsChave) throws SQLException {
		DocumentoAnexo documentoAnexo = new DocumentoAnexo();
		documentoAnexo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		documentoAnexo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		documentoAnexo.nmEntidade = nmEntidade;
		documentoAnexo.dsChave = dsChave;
		return countByExample(documentoAnexo) > 0;
	}
	
	public void validaAnexoPedidoCondicaoPagamento(Pedido pedido) throws SQLException, ValidationException {
		if (LavenderePdaConfig.isObrigaAnexoDocCondicaoPagamento() && pedido.getCondicaoPagamento().isObrigaAnexoDocumento()) {
			if (!hasAnexoNoPedido(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey())) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_PEDIDO_SEM_ANEXO, pedido.getCondicaoPagamento().dsCondicaoPagamento));
			}
		}
	}
	
	public int getIdGlobal() throws SQLException {
		return ValueUtil.getIntegerValue(generateIdGlobal());
	}
}