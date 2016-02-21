package com.algaworks.boleto;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

public class PrimeiroBoleto {

	public static void main(String[] args) {
		// Cedente
		Cedente cedente = new Cedente("Gabriel Zanlorenzi", "056.950.639-57");

		// Sacado
		Sacado sacado = new Sacado("Fernando da Silva");

		// Endere�o do sacado
		Endereco endereco = new Endereco();
		endereco.setUF(UnidadeFederativa.SP);
		endereco.setLocalidade("Ourinhos");
		endereco.setCep("19910-200");
		endereco.setBairro("Jardim das paineiras");
		endereco.setLogradouro("Rua Irineu Pereira da Silva ");
		endereco.setNumero("700");

		sacado.addEndereco(endereco);

		// Criando o t�tulo
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
		contaBancaria.setAgencia(new Agencia(1010, "0"));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(2020, "0"));
		// 'carteira 6' significa que � um boleto sem registro, o banco n�o tem
		// conci�ncia que est� sendo gerado, por�m � v�lido
		contaBancaria.setCarteira(new Carteira(6));

		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento("1010");
		titulo.setNossoNumero("12345678901");
		titulo.setDigitoDoNossoNumero("P");

		titulo.setValor(BigDecimal.valueOf(100.23));
		titulo.setDataDoDocumento(new Date());

		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 2, 20);
		titulo.setDataDoVencimento(calendar.getTime());

		// tipo de neg�cio da empresa, informa��o que consegue com o analista e
		// o gerente do banco
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);

		// Se a pessoa que comprou assinou alguma coisa para gerar esse boleto
		titulo.setAceite(Aceite.N);

		// Dados do boleto
		Boleto boleto = new Boleto(titulo);
		boleto.setLocalPagamento("Pagar preferencialemnte no bando do Bradesco.");
		boleto.setInstrucaoAoSacado("Evite Multas, pague em dias suas contas.");

		boleto.setInstrucao1("Ap�s o vencimento, aplicar multa de 2,00% e juros de 1,00% ao m�s");

		BoletoViewer boletoViewer = new BoletoViewer(boleto);
		File arquivoPdf = boletoViewer.getPdfAsFile("Meu-primeiro-boleto.pdf");

		mostrarNaTela(arquivoPdf);
	}

	private static void mostrarNaTela(File arquivo) {
		Desktop desktop = Desktop.getDesktop();

		try {
			desktop.open(arquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
