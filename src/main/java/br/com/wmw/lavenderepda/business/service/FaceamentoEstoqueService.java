package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.sql.Types;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FaceamentoEstoque;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FaceamentoEstoquePdbxDao;
import totalcross.util.Vector;

public class FaceamentoEstoqueService extends CrudService {

    private static FaceamentoEstoqueService instance;

    private FaceamentoEstoqueService() {
        //--
    }

    public static FaceamentoEstoqueService getInstance() {
        if (instance == null) {
            instance = new FaceamentoEstoqueService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FaceamentoEstoquePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }


	public Vector findAllFaceamentoEstoqueByDataAtual(Pedido pedido) throws SQLException {
		FaceamentoEstoque faceamentoEstoque = new FaceamentoEstoque();
		faceamentoEstoque.cdEmpresa = pedido.cdEmpresa;
		faceamentoEstoque.cdRepresentante = pedido.cdRepresentante;
		faceamentoEstoque.cdCliente = pedido.cdCliente;
		faceamentoEstoque.dtCadastro = DateUtil.getCurrentDate();
		faceamentoEstoque.flUltilizadoPedidoDtAtual = ValueUtil.VALOR_NAO;
		return FaceamentoEstoqueService.getInstance().findAllByExample(faceamentoEstoque);
	}

	public void findAllAndUpdateFaceamentoEstoqueByItemPedido(String cdCliente, String cdProduto) throws SQLException {
		FaceamentoEstoque faceamentoEstoque = findFaceamentoEstoque(cdCliente, cdProduto);
		if (faceamentoEstoque == null) return;
		
		faceamentoEstoque.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
		faceamentoEstoque.flUltilizadoPedidoDtAtual = ValueUtil.VALOR_NAO;
		FaceamentoEstoqueService.getInstance().update(faceamentoEstoque);
	}

	private FaceamentoEstoque findFaceamentoEstoque(String cdCliente, String cdProduto) throws SQLException {
		FaceamentoEstoque faceamentoEstoque = new FaceamentoEstoque(SessionLavenderePda.cdEmpresa, DateUtil.getCurrentDate());
		faceamentoEstoque.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		faceamentoEstoque.cdCliente = cdCliente; 
		faceamentoEstoque.cdProduto = cdProduto;
		faceamentoEstoque = (FaceamentoEstoque) FaceamentoEstoqueService.getInstance().findByRowKey(faceamentoEstoque.getRowKey());
		return faceamentoEstoque;
	}
	
	public double calculaSugestaoVenda(double qtEstoqueAtual, double vlFatorFaceamento, double pontoEquilibrio) {
		return calculaSugestaoVenda(qtEstoqueAtual, vlFatorFaceamento, pontoEquilibrio, LavenderePdaConfig.isUsaQtdInteiro() ? 0 : LavenderePdaConfig.nuCasasDecimais);
	}
	
	public double calculaSugestaoVenda(double qtEstoqueAtual, double vlFatorFaceamento, double pontoEquilibrio, int precisao) {
		vlFatorFaceamento = vlFatorFaceamento > 0 ? vlFatorFaceamento : 1;
		double qtSugestaoVenda = ValueUtil.round((pontoEquilibrio - qtEstoqueAtual) * vlFatorFaceamento, precisao);
		return qtSugestaoVenda > 0 ? qtSugestaoVenda : 0;
	}

	public void recalculaSugestaoVenda(double pontoEquilibrio) throws SQLException {
		Vector faceamentoEstoqueList = buscaFaceamentoEstoqueDoDiaList();
		int size = faceamentoEstoqueList.size();
		for (int i = 0; i < size; i++) {
			FaceamentoEstoque faceamentoEstoque = (FaceamentoEstoque) faceamentoEstoqueList.items[i];
			double qtSugestaoVenda = calculaSugestaoVenda(faceamentoEstoque.qtEstoqueAtual, faceamentoEstoque.vlFatorFaceamento, pontoEquilibrio);
			updateColumn(faceamentoEstoque.rowKey, "QTSUGESTAOVENDA", qtSugestaoVenda, Types.DECIMAL);
		}
	}
	
	private Vector buscaFaceamentoEstoqueDoDiaList() throws SQLException {
		FaceamentoEstoque faceamentoEstoqueFilter = new FaceamentoEstoque(SessionLavenderePda.cdEmpresa, DateUtil.getCurrentDate());
		return findAllByExample(faceamentoEstoqueFilter);
	}

}