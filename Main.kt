import java.io.File

const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun menuPrincipal(): Int {
    println()
    println("""> > Batalha Naval < <
        |
        |1 - Definir Tabuleiro e Navios
        |2 - Jogar
        |3 - Gravar
        |4 - Ler
        |0 - Sair
        |
    """.trimMargin())
    var menuEscolhido: Int?
    do {
        menuEscolhido = readln().toIntOrNull()
        if (menuEscolhido == null || menuEscolhido >= 5 || menuEscolhido < -1) {
            println("!!! Opcao invalida, tente novamente")
        }
    } while (menuEscolhido == null || menuEscolhido >= 5 || menuEscolhido < -1)

    when (menuEscolhido) {
        1 -> return MENU_DEFINIR_TABULEIRO
        2, 3, 4 -> {
            println("!!! POR IMPLEMENTAR, tente novamente")
            return MENU_PRINCIPAL
        }

        -1 -> return MENU_PRINCIPAL
        0 -> return SAIR
    }
    println()
    return menuEscolhido
}


fun menuDefinirTabuleiro(): Int {
    println()
    println("""> > Batalha Naval < <
        |
        |Defina o tamanho do tabuleiro:
    """.trimMargin())
    var linhas: Int?
    var colunas: Int?
    var orientacao: String?

    do {

        do {
            println("Quantas linhas?")
            linhas = readln().toIntOrNull()

            when (linhas) {
                null -> println("!!! Número de linhas invalidas, tente novamente")
                -1 -> return MENU_PRINCIPAL
                0 -> return SAIR
            }
        } while (linhas == null)

        do {
            println("Quantas colunas?")
            colunas = readln().toIntOrNull()

            when (colunas) {
                null -> println("!!! Número de linhas invalidas, tente novamente")
                -1 -> return MENU_PRINCIPAL
                0 -> return SAIR
            }
        } while (colunas == null)

    } while (!tamanhoTabuleiroValido(linhas, colunas))

    if (linhas != null && colunas != null) {

        tabuleiroHumano = criaTabuleiroVazio(linhas, colunas)
        val mapa = obtemMapa(tabuleiroHumano, true)
        for (linha in mapa) print(linha)

        val numDeNavios = calculaNumNavios(linhas, colunas)
        val tiposDeNavios = arrayOf("submarino", "contra-torpedeiro", "navio-tanque", "porta-avioes")

        for (posicao in 0 until numDeNavios.size) {
            for (count in 0 until numDeNavios[posicao]) {

                if (numDeNavios[posicao] != 0) {
                    var coordenadas: String?

                    println("""Insira as coordenadas de um ${tiposDeNavios[posicao]}:
            |Coordenadas? (ex: 6,G)
        """.trimMargin())
                    do {
                        coordenadas = readlnOrNull()

                        if (coordenadas != null && coordenadas != "") {
                            if (coordenadas.toIntOrNull() == -1) return MENU_PRINCIPAL
                            if (coordenadas.toIntOrNull() == 0) return SAIR
                        }
                    } while (processaCoordenadas(coordenadas, linhas, colunas) == null)

                    val coordenadasNum = processaCoordenadas(coordenadas, linhas, colunas)

                    if (coordenadasNum != null && posicao == 0) {
                        insereNavioSimples(tabuleiroHumano, coordenadasNum.first, coordenadasNum.second, 1)
                    } else if (posicao > 0) {
                        println("""Insira a orientacao do navio:
        |Orientacao? (N, S, E, O)
    """.trimMargin())
                        do {
                            orientacao = readlnOrNull()

                            if (orientacao != null && orientacao != "") {
                                when {
                                    orientacao.toIntOrNull() == -1 -> return MENU_PRINCIPAL
                                    orientacao.toIntOrNull() == 0 -> return SAIR
                                    orientacao != "E" && orientacao != "O" && orientacao != "N" && orientacao != "S" -> {
                                        orientacao = null
                                        println("""!!! Orientacao invalida, tente novamente
                |Orientacao? (N, S, E, O)
            """.trimMargin())
                                    }
                                }
                            }
                        } while (orientacao == null)

                        if (coordenadasNum != null) {
                            insereNavio(tabuleiroHumano,
                                    coordenadasNum.first,
                                    coordenadasNum.second, orientacao,
                                    posicao + 1)
                        }
                    }
                    val mapa2 = obtemMapa(tabuleiroHumano, true)
                    for (linha in mapa2) print(linha)
                }
            }
        }
    }
    return MENU_PRINCIPAL
}


fun menuDefinirNavios(): Int {
    return MENU_PRINCIPAL

}

fun menuJogar(): Int {
    return MENU_PRINCIPAL
}

fun menuLerFicheiro(): Int {
    return MENU_PRINCIPAL
}

fun menuGravarFicheiro(): Int {
    return MENU_PRINCIPAL
}


fun tamanhoTabuleiroValido(numLinhas: Int?, numColunas: Int?): Boolean {
    return when {
        numColunas == 4 && numLinhas == 4 -> true
        numColunas == 5 && numLinhas == 5 -> true
        numColunas == 7 && numLinhas == 7 -> true
        numColunas == 8 && numLinhas == 8 -> true
        numColunas == 10 && numLinhas == 10 -> true
        else -> {
            println("!!! Tamanho de tabuleiro invalido")
            return false
        }
    }
}


fun processaCoordenadas(coordenadas: String?, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {


    if (coordenadas == null || coordenadas.length < 3 || coordenadas.length > 4) {
        println("""!!! Coordenadas invalidas, tente novamente
            |Coordenadas? (ex: 6,G)
        """.trimMargin())
        return null
    } else {

        val colunaCode = coordenadas[coordenadas.length - 1].code
        var linha = coordenadas[0].toString().toIntOrNull()

        if (coordenadas.length == 3 && coordenadas[1] == ',' && colunaCode in 65..90) {
            if (linha != null) {
                return when {
                    linha == 0 -> null
                    linha <= numLinhas && colunaCode <= numColunas + 64 -> Pair(linha, colunaCode - 64)
                    else -> {
                        println("""!!! Coordenadas invalidas, tente novamente
                |Coordenadas? (ex: 6,G)
            """.trimMargin())
                        return null
                    }
                }
            }
        }
        if (coordenadas.length == 4 && coordenadas[2] == ',' && colunaCode in 65..90) {

            linha = (coordenadas[0].toString() + coordenadas[1].toString()).toInt()

            return when {
                linha == 0 -> null
                linha <= numLinhas && colunaCode <= numColunas + 64 -> Pair(linha, colunaCode - 64)
                else -> null
            }
        }
        println("""!!! Coordenadas invalidas, tente novamente
                |Coordenadas? (ex: 6,G)
            """.trimMargin())
        return null
    }
}


fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 0
    var codigoAsciiLetraA = 65
    var legendaHorizontal = ""
    while (count < numColunas) {
        legendaHorizontal += if (count == numColunas - 1) {
            "${codigoAsciiLetraA.toChar()}"
        } else {
            "${codigoAsciiLetraA.toChar()} | "
        }
        count++
        codigoAsciiLetraA++
    }
    return legendaHorizontal
}

fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    var linha = 0
    var terreno = ""
    terreno += "\n| "
    terreno += (criaLegendaHorizontal(numColunas))
    terreno += " |\n"
    while (linha < numLinhas) {
        var coluna = 0
        while (coluna < numColunas) {
            terreno += "|   "

            if (coluna == numColunas - 1) {
                terreno += "| ${linha + 1}"
            }
            coluna++
        }
        terreno += "\n"
        linha++
    }
    return terreno
}


fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {

    val numNavios: Array<Int> = when {
        numLinhas == 4 && numColunas == 4 -> arrayOf(2, 0, 0, 0)
        numLinhas == 5 && numColunas == 5 -> arrayOf(1, 1, 1, 0)
        numLinhas == 7 && numColunas == 7 -> arrayOf(2, 1, 1, 1)
        numLinhas == 8 && numColunas == 8 -> arrayOf(2, 2, 1, 1)
        numLinhas == 10 && numColunas == 10 -> arrayOf(3, 2, 1, 1)
        else -> emptyArray()

    }
    return numNavios
}


fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    return Array(numLinhas) { Array(numColunas) { null } }
}


fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    return linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size
}
//a linha e a coluna começam em 1


fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var coordenadasLimpas = emptyArray<Pair<Int, Int>>()

    for (coordenada in coordenadas)
        if (coordenada != Pair(0, 0)) {
            coordenadasLimpas += coordenada
        }
    return coordenadasLimpas
}


fun juntarCoordenadas(coordenadas: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var coordenadasJuntas = coordenadas

    for (coordenada in coordenadas2)
        coordenadasJuntas += coordenada

    return coordenadasJuntas
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>,
                          linha: Int,
                          coluna: Int,
                          orientacao: String,
                          dimensao: Int): Array<Pair<Int, Int>> {

    var coordenadasNavio = emptyArray<Pair<Int, Int>>()
    var possiveisCoordenadasNavio = emptyArray<Pair<Int, Int>>()

    if (linha != 0 && coluna != 0) {
        when (orientacao) {
            "E" -> for (posicao in coluna - 1..coluna + 2) {
                if (coordenadaContida(tabuleiro, linha, posicao + 1)) {
                    possiveisCoordenadasNavio += Pair(linha, posicao + 1)
                }
            }

            "N" -> for (posicao in linha - 1 downTo linha - 4) {
                if (coordenadaContida(tabuleiro, posicao + 1, coluna)) {
                    possiveisCoordenadasNavio += Pair(posicao + 1, coluna)
                }
            }

            "O" -> for (posicao in coluna - 1 downTo coluna - 4) {
                if (coordenadaContida(tabuleiro, linha, posicao + 1)) {
                    possiveisCoordenadasNavio += Pair(linha, posicao + 1)
                }
            }

            "S" -> {
                for (posicao in linha - 1..linha + 2) {
                    if (coordenadaContida(tabuleiro, posicao + 1, coluna)) {
                        possiveisCoordenadasNavio += Pair(posicao + 1, coluna)
                    }
                }
            }
        }
    } else return coordenadasNavio

    when {
        possiveisCoordenadasNavio.size < dimensao -> return coordenadasNavio
        possiveisCoordenadasNavio.isEmpty() -> return coordenadasNavio
        else -> {
            for (posicao in 0 until dimensao)
                coordenadasNavio += possiveisCoordenadasNavio[posicao]
        }

    }
    return coordenadasNavio
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>,
                              linha: Int,
                              coluna: Int,
                              orientacao: String,
                              dimensao: Int): Array<Pair<Int, Int>> {

    var coordenadasFronteira: Array<Pair<Int, Int>> = emptyArray()

    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)

    for (coordenadas in coordenadasNavio) {

        val linhaNavioNaMatriz = coordenadas.first
        val colunaNavioNaMatriz = coordenadas.second

        for (i in linhaNavioNaMatriz - 1..linhaNavioNaMatriz + 1) {
            for (j in colunaNavioNaMatriz - 1..colunaNavioNaMatriz + 1) {

                if (coordenadaContida(tabuleiro, i, j)) {
                    if (
                            (Pair(i, j) !in coordenadasNavio) &&
                            (Pair(i, j) !in coordenadasFronteira)
                    ) {
                        coordenadasFronteira += Pair(i, j)


                    }
                }

            }

        }


    }
    return coordenadasFronteira
}


fun estaLivre(tabuleiro: Array<Array<Char?>>, conjuntoDeCoordenadas: Array<Pair<Int, Int>>): Boolean {
    for (coordenada in conjuntoDeCoordenadas) {
        if (tabuleiro[coordenada.first - 1][coordenada.second - 1] != null) return false
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    //assume sempre orientação Este

    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao)
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    val coordenadasNavioEFronteira = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)

    if (estaLivre(tabuleiro, coordenadasNavioEFronteira)) {
        for (coordenada in coordenadasNavio)
            tabuleiro[coordenada.first - 1][coordenada.second - 1] = dimensao.toString().first()
        return true
    }

    return false

}


fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    //quando receber a orientaçao E usar a fun inserirNavioSimples


    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    val coordenadasNavioEFronteira = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)

    if (orientacao == "E") {
        insereNavioSimples(tabuleiro, linha, coluna, dimensao)
    } else if (estaLivre(tabuleiro, coordenadasNavioEFronteira)) {
        for (coordenada in coordenadasNavio)
            tabuleiro[coordenada.first - 1][coordenada.second - 1] = dimensao.toString().first()
        return true


    }

    return false

}


fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, numeroDeNavios: Array<Int>): Array<Array<Char?>> {


    for (posicao in 0 until numeroDeNavios.size) {
        for (count in 1..numeroDeNavios[posicao]) {
            do {

                val linhaAleatoria = (0 until tabuleiroComputador.size).random()
                val colunaAleatoria = (0 until tabuleiroComputador[linhaAleatoria].size).random()
                val orientacaoAleatoria = arrayOf("E", "N", "S", "O").random()


                val controlo = insereNavio(tabuleiroComputador,
                        linhaAleatoria,
                        colunaAleatoria,
                        orientacaoAleatoria,
                        posicao + 1)

            } while (!controlo)

        }
    }
    return tabuleiroComputador
}

fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {

    val dimensao = tabuleiroPalpites[linha - 1][coluna - 1].toString().toIntOrNull() ?: return false

    var coordenadasNavio = emptyArray<Pair<Int, Int>>()

    val linhaMinima = (linha - dimensao) + 1
    val linhaMaxima = linha + dimensao
    val colunaMinima = (coluna - dimensao) + 1
    val colunaMaxima = coluna + dimensao

    for (i in linhaMinima until linhaMaxima) {
        if (coordenadaContida(tabuleiroPalpites, i, coluna - 1) &&
                tabuleiroPalpites[i][coluna - 1] == dimensao.toString()[0]) {
            coordenadasNavio += Pair(i, coluna - 1)
        }
    }
    if (coordenadasNavio.size == dimensao) return true
    if (coordenadasNavio.size < dimensao) return false

    if (coordenadasNavio.isEmpty()) {
        for (j in colunaMinima until colunaMaxima) {
            if (coordenadaContida(tabuleiroPalpites, linha - 1, j) &&
                    tabuleiroPalpites[linha - 1][j] == dimensao.toString()[0]) {
                coordenadasNavio += Pair(linha - 1, j)
            }
        }
        if (coordenadasNavio.size == dimensao) return true
        if (coordenadasNavio.size < dimensao) return false
    }
    return false
}


fun obtemMapa(tabuleiroReal: Array<Array<Char?>>, eTabuleiroReal: Boolean): Array<String> {
    //Substitui criaTerreno()

    val mapa = Array(tabuleiroReal.size + 1) { "|" }

    val codAscii2 = '\u2082'
    val codAscii3 = '\u2083'
    val codAscii4 = '\u2084'


    mapa[0] += " ${criaLegendaHorizontal(tabuleiroReal[0].size)} |\n"

    for (linha in 0 until tabuleiroReal.size) {
        var linhaDoMapa = "|"
        for (coluna in 0 until tabuleiroReal[linha].size) {

            val valor = tabuleiroReal[linha][coluna]

            when (tabuleiroReal[linha][coluna]) {
                null -> {
                    linhaDoMapa += if (eTabuleiroReal) " ~ |" else " ? |"
                }

                'X' -> linhaDoMapa += " X |"
                '1', '2', '3', '4' -> {
                    if (eTabuleiroReal) linhaDoMapa += " $valor |" else {
                        if (navioCompleto(tabuleiroReal, linha, coluna)) linhaDoMapa += " $valor |" else {
                            when (valor) {
                                '2' -> linhaDoMapa += " $codAscii2 |"
                                '3' -> linhaDoMapa += " $codAscii3 |"
                                '4' -> linhaDoMapa += " $codAscii4 |"
                            }
                        }
                    }
                }

            }
            if (coluna == tabuleiroReal[linha].size - 1) linhaDoMapa += " ${linha + 1} \n"
        }
        mapa[linha + 1] = linhaDoMapa

    }
    return mapa
}

fun lancarTiro(tabuleiroReal: Array<Array<Char?>>,
               tabuleiroPalpites: Array<Array<Char?>>,
               linhaEColuna: Pair<Int, Int>): String {

    var mensagem = ""

    when (tabuleiroReal[linhaEColuna.first][linhaEColuna.second]) {
        null -> {
            tabuleiroPalpites[linhaEColuna.first][linhaEColuna.second] = 'X'
            mensagem = "Agua."
        }

        '1' -> {
            tabuleiroPalpites[linhaEColuna.first][linhaEColuna.second] = '1'
            mensagem = "Tiro num submarino."
        }

        '2' -> {
            tabuleiroPalpites[linhaEColuna.first][linhaEColuna.second] = '2'
            mensagem = "Tiro num contra-torpedeiro."
        }

        '3' -> {
            tabuleiroPalpites[linhaEColuna.first][linhaEColuna.second] = '3'
            mensagem = "Tiro num navio-tanque."
        }

        '4' -> {
            tabuleiroPalpites[linhaEColuna.first][linhaEColuna.second] = '4'
            mensagem = "Tiro num porta-avioes."
        }

        else -> mensagem = "Agua."
    }



    return mensagem
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {

    var coordenadasNull = emptyArray<Pair<Int, Int>>()

    for (linha in 0 until tabuleiroPalpitesComputador.size) {
        for (coluna in 0 until tabuleiroPalpitesComputador[linha].size) {
            if (tabuleiroPalpitesComputador[linha][coluna] == null) coordenadasNull += Pair(linha, coluna)
        }
    }

    val tiroComputador = coordenadasNull.random()

    return tiroComputador
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {

    var count = 0

    for (linha in 0 until tabuleiro.size) {
        for (coluna in 0 until tabuleiro[linha].size) {

            if (tabuleiro[linha][coluna] == dimensao.toChar()) {
                if (navioCompleto(tabuleiro, linha, coluna)) count++
            }
        }
    }
    when (dimensao) {
        2 -> count /= 2
        3 -> count /= 3
        4 -> count /= 4
    }
    return count
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {

    return (contarNaviosDeDimensao(tabuleiro, 1) == calculaNumNavios(tabuleiro.size, tabuleiro[0].size)[0]
            && contarNaviosDeDimensao(tabuleiro, 2) == calculaNumNavios(tabuleiro.size, tabuleiro[0].size)[1]
            && contarNaviosDeDimensao(tabuleiro, 3) == calculaNumNavios(tabuleiro.size, tabuleiro[0].size)[2]
            && contarNaviosDeDimensao(tabuleiro, 4) == calculaNumNavios(tabuleiro.size, tabuleiro[0].size)[3])

}

fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): Array<Array<Char?>> {

    val ficheiro = File("nomeDoFicheiro.csv").readLines()

    when (tipoDeTabuleiro) {
        1 -> {
            for (i in 0 until ficheiro.size) {
                val linha = ficheiro[i].split(",")

            }
        }

    }

    return Array(0) { Array(0) { ' ' } }
}

fun gravarJogo(nomeDoFicheiro: String,
               tabuleiroRealHumano: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiroPalpitesComputador: Array<Array<Char?>>) {

}

fun main() {
    var menuAtual = MENU_PRINCIPAL
    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}