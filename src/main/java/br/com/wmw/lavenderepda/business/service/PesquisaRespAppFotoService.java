package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppFoto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaRespAppFotoPdbxDao;
import totalcross.util.Vector;

public class PesquisaRespAppFotoService extends CrudService {

    private static PesquisaRespAppFotoService instance;

    private PesquisaRespAppFotoService() {
        //--
    }

    public static PesquisaRespAppFotoService getInstance() {
        if (instance == null) {
            instance = new PesquisaRespAppFotoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PesquisaRespAppFotoPdbxDao.getInstance();
    }

	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public Vector findAllPesquisaRespAppFotoNaoEnviadosServidor() throws SQLException {
		return PesquisaRespAppFotoPdbxDao.getInstance().findAllImagesToSend();
	}
	
	public void insertByPesquisaAndPerguntaResposta(PesquisaApp pesquisaApp, PerguntaResposta perguntaResposta) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFoto = new PesquisaRespAppFoto();
		pesquisaRespAppFoto.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFoto.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFoto.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFoto.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFoto.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppFoto.cdPergunta = perguntaResposta.cdPergunta;
		pesquisaRespAppFoto.cdResposta = perguntaResposta.cdResposta;
		pesquisaRespAppFoto.imFoto = perguntaResposta.pesquisaRespAppFoto.imFoto;
		pesquisaRespAppFoto.flEnviadoServidor = ValueUtil.VALOR_NAO;
		pesquisaRespAppFoto.flTipoAlteracao = PesquisaRespAppFoto.FLTIPOALTERACAO_INSERIDO;
		pesquisaRespAppFoto.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		insert(pesquisaRespAppFoto);
	}
	
	public void deleteAllByPesquisaApp(PesquisaApp pesquisaApp) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFotoFilter = new PesquisaRespAppFoto();
		pesquisaRespAppFotoFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFotoFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFotoFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFotoFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFotoFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		Vector imFotoList = findListImFoto(pesquisaRespAppFotoFilter);
		PesquisaRespAppFotoPdbxDao.getInstance().deleteAllByExample(pesquisaRespAppFotoFilter);
		deleteListFotoLocal(imFotoList);
	}
	
	public void deleteFotoLocal(PesquisaRespAppFoto pesquisaRespAppFoto) {
		if (pesquisaRespAppFoto != null) {
			deleteFotoLocal(pesquisaRespAppFoto.imFoto);
		}
	}
	
	public void deleteFotoLocal(String imFoto) {
		if (ValueUtil.isNotEmpty(imFoto)) {
			String fileName = PesquisaRespAppFoto.getPathImg() + imFoto;
			FileUtil.deleteFile(fileName);
		}
	}
	
	private void deleteListFotoLocal(Vector imFotoList) {
		int size = imFotoList.size();
		for (int i = 0; i < size; i++) {
			PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) imFotoList.items[i];
			deleteFotoLocal(pesquisaRespAppFoto.imFoto);
		}
	}
	
	private Vector findListImFoto(BaseDomain domain) throws SQLException {
		return PesquisaRespAppFotoPdbxDao.getInstance().findListImFoto(domain);
	}
	
	public void atualizaFotoAposEnvio(Vector imFotoList, String flEnviadoServidor) throws SQLException {
		int size = imFotoList.size();
		for (int i = 0; i < size; i++) {
			String imFoto = (String) imFotoList.items[i];
			PesquisaRespAppFoto pesquisaRespAppFotoFilter = new PesquisaRespAppFoto();
			pesquisaRespAppFotoFilter.imFoto = imFoto;
			Vector pesquisaRespAppFotoList = findAllByExample(pesquisaRespAppFotoFilter);
			if (ValueUtil.isNotEmpty(pesquisaRespAppFotoList)) {
				PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) pesquisaRespAppFotoList.items[0];
				pesquisaRespAppFoto.flEnviadoServidor = flEnviadoServidor;
				try {
					update(pesquisaRespAppFoto);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector pesquisaRespAppFotoList = PesquisaRespAppFotoPdbxDao.getInstance().findAllImagesToSend();
		int size = pesquisaRespAppFotoList.size();
		for (int i = 0; i < size; i++) {
			PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) pesquisaRespAppFotoList.items[i];
			imageList.addElement(pesquisaRespAppFoto.imFoto);
		}
		return imageList;
	}
	
	public boolean isExisteFotoByPesquisaAppAndPerguntaResposta(PesquisaApp pesquisaApp, PerguntaResposta perguntaResposta) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFotoFilter = new PesquisaRespAppFoto();
		pesquisaRespAppFotoFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFotoFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFotoFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFotoFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFotoFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppFotoFilter.cdPergunta = perguntaResposta.cdPergunta;
		pesquisaRespAppFotoFilter.cdResposta = perguntaResposta.cdResposta;
		return countByExample(pesquisaRespAppFotoFilter) > 0;
	}
	
	public void deleteAllByPesquisaAppAndPerguntaResposta(PesquisaApp pesquisaApp, PerguntaResposta perguntaResposta) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFotoFilter = new PesquisaRespAppFoto();
		pesquisaRespAppFotoFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFotoFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFotoFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFotoFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFotoFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppFotoFilter.cdPergunta = perguntaResposta.cdPergunta;
		pesquisaRespAppFotoFilter.cdResposta = perguntaResposta.cdResposta;
		Vector imFotoList = findListImFoto(pesquisaRespAppFotoFilter);
		deleteAllByExample(pesquisaRespAppFotoFilter);
		deleteListFotoLocal(imFotoList);
	}
	
	public void deleteAllByPesquisaAppAndPerguntaRespostaDiferente(PesquisaApp pesquisaApp, String cdPergunta, String cdResposta) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFotoFilter = new PesquisaRespAppFoto();
		pesquisaRespAppFotoFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFotoFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFotoFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFotoFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFotoFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppFotoFilter.cdPergunta = cdPergunta;
		pesquisaRespAppFotoFilter.cdRespostaDiferente = cdResposta;
		Vector imFotoList = findListImFoto(pesquisaRespAppFotoFilter);
		deleteAllByExample(pesquisaRespAppFotoFilter);
		deleteListFotoLocal(imFotoList);
	}
	
}