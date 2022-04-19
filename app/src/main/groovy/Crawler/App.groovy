/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package Crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class App {
    def taskUm() {
        Document doc = Jsoup.connect("https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/padrao-tiss-2013-marco-2022").timeout(5000).get()
        Elements body = doc.select("tbody")
        int valorArquivo = 1
        for (e in body.select("tr td a")) {
            String internallinks = e.select(".internal-link").attr("href")
            String externalLinks = e.select(".external-link").attr("href")
            if (e.select(".internal-link").attr("class") == "btn btn-primary btn-sm center-block internal-link") {
                URL links = new  URL(internallinks)
                ReadableByteChannel rbc = Channels.newChannel(links.openStream())
                if (internallinks.substring(internallinks.length() - 1) == "f") { // Essa gambiarra aqui me diz se é um arquivo pdf ou um ZIP
                    FileOutputStream fos = new FileOutputStream("arquivo${valorArquivo}.pdf")
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
                } else  {
                    FileOutputStream fos = new FileOutputStream("arquivo${valorArquivo}.zip")
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
                }


            }

            else if(e.select(".external-link").attr("class") == "btn btn-primary btn-sm center-block external-link") {
                println(externalLinks)
                externalLinks = "https://www.ans.gov.br/arquivos/extras/tiss/Padr%C3%A3o%20TISS_Representa%C3%A7%C3%A3o%20de%20Conceitos%20em%20Sa%C3%BAde_202203.zip" // Por algum motivo o link vinha quebrado quando eu pegava ele pelo href, então fiz isso aqui como um teste, pode ser que tenha que ficar na versão final.
                externalLinks = externalLinks.replaceAll("\\s+","")
                println(externalLinks)
                URL links = new  URL(externalLinks)
                ReadableByteChannel rbc = Channels.newChannel(links.openStream())
                if (externalLinks.substring(externalLinks.length() - 1) == "f") { // Essa gambiarra aqui me diz se é um arquivo pdf ou um ZIP
                    FileOutputStream fos = new FileOutputStream("arquivo${valorArquivo}.pdf")
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
                } else  {
                    FileOutputStream fos = new FileOutputStream("arquivo${valorArquivo}.zip")
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }



         //Vou comentar essa parte aqui por que o arquivo é 500mb e demora 20 anos pra baixar. Vou só deixar a url printada


            valorArquivo += 1
        }
        println("=====  Fim da Task um  =====")

    }


    def taskDois() {
        Document doc = Jsoup.connect("https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss").timeout(5000).get()
        Elements body = doc.select("tbody")
        int contadorDeLoopExterno = 0
        int tamanhoTableRows = body.select("tr").size()
        for (e in body.select("tr")){
            println("=======================================================================")
            int contadorDeLoop = 1

            for (x in e.select("tr td")) {
                String competencia = x.select("td").text()

                if (contadorDeLoop == 1) {
                    println("Competência: ${competencia}")
                } else if(contadorDeLoop == 2) {
                    println("Data da publicação: ${competencia}")
                } else {
                    println("Inicio de vigência: ${competencia}")
                }
                contadorDeLoop += 1
                if (contadorDeLoop == 4) {
                    break
                }
            }

            contadorDeLoopExterno += 1
            if (contadorDeLoopExterno == tamanhoTableRows-4) {
                break
            }
        }
        println("=====  Fim da Task dois  =====")

    }
    def taskTres(){
        Document doc = Jsoup.connect("https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/padrao-tiss-tabelas-relacionadas").timeout(5000).get()
        Elements body = doc.select("#parent-fieldname-text")
        String linkDocumentoANS = body.select(".internal-link").attr("href")
        URL links = new URL(linkDocumentoANS)
        ReadableByteChannel rbc = Channels.newChannel(links.openStream())
        FileOutputStream fos = new FileOutputStream("TabelaDeErros.xlsx")
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)

        println("=====  Fim da Task tres  =====")
    }


    static void main(String[] args) {
        new App().taskUm()
        new App().taskDois()
        new App().taskTres()
    }

}
