### TempoDica-main\build.gradle.kts

```
// Este é um comentário de nível superior em um arquivo 'build.gradle.kts' (Gradle Kotlin DSL).
// Geralmente, este arquivo é o 'build.gradle.kts' localizado na raiz do projeto (top-level build file).
// Ele é usado para definir configurações globais, repositórios de dependências, e declarar plugins
// que serão comuns ou disponíveis para todos os submódulos do projeto.
// A frase original "Top-level build file where you can add configuration options common to all sub-projects/modules."
// descreve exatamente o propósito deste arquivo.

plugins {
    // Este bloco 'plugins' é uma característica do Gradle que permite a declaração e aplicação de plugins
    // de forma declarativa. Em um arquivo 'build.gradle.kts' de nível superior, é comum declarar plugins
    // que serão usados por submódulos, mas sem aplicá-los diretamente ao projeto raiz.
    // Isso é feito para gerenciar as versões dos plugins centralmente usando Version Catalogs (libs.versions.toml)
    // e garantir que todos os submódulos usem a mesma versão do plugin.

    alias(libs.plugins.android.application) apply false
    // Esta linha declara o plugin 'com.android.application', que é o plugin principal para construir
    // aplicativos Android.
    // - 'alias': É uma função do Gradle Kotlin DSL que permite referenciar um plugin usando um alias.
    //   Este alias (neste caso, 'libs.plugins.android.application') é definido em um arquivo 'libs.versions.toml'
    //   (um "Version Catalog"), que centraliza o gerenciamento de versões de dependências e plugins.
    //   'libs.plugins.android.application' resolve para o ID real do plugin, como "com.android.application",
    //   e sua versão definida no 'libs.versions.toml'.
    // - 'apply false': Indica que este plugin NÃO deve ser aplicado diretamente a este projeto de nível superior.
    //   Em vez disso, ele está sendo *declarado* aqui para que qualquer submódulo (como o módulo 'app')
    //   possa aplicá-lo usando `id("com.android.application")` ou `alias(libs.plugins.android.application)`
    //   em seu próprio arquivo 'build.gradle.kts'. Isso promove o reuso de versões e configurações.

    alias(libs.plugins.kotlin.android) apply false
    // Esta linha declara o plugin 'org.jetbrains.kotlin.android', que é essencial para projetos Android
    // que utilizam a linguagem de programação Kotlin.
    // - 'alias(libs.plugins.kotlin.android)': Referencia o plugin Kotlin Android através do Version Catalog.
    //   Ele resolve para o ID do plugin "org.jetbrains.kotlin.android" e sua versão. Este plugin
    //   configura o compilador Kotlin para trabalhar com o ambiente de build do Android.
    // - 'apply false': Semelhante ao plugin de aplicação Android, este plugin é declarado para ser
    //   disponível para submódulos que usam Kotlin, mas não é aplicado diretamente ao projeto raiz.

    alias(libs.plugins.compose.compiler) apply false
    // Esta linha declara o plugin 'org.jetbrains.kotlin.plugin.compose', que é o plugin do compilador
    // Kotlin para o Jetpack Compose. É fundamental para projetos que utilizam o Jetpack Compose
    // para construir suas interfaces de usuário no Android.
    // - 'alias(libs.plugins.compose.compiler)': Referencia o plugin do compilador Compose através do Version Catalog.
    //   Ele resolve para o ID do plugin "org.jetbrains.kotlin.plugin.compose" e sua versão. Este plugin
    //   adiciona transformações e otimizações necessárias para o código `@Composable` do Jetpack Compose.
    // - 'apply false': Declara o plugin para ser utilizado pelos submódulos que desenvolvem UI com Compose,
    //   mas não o aplica ao projeto de nível superior.
}
```

### TempoDica-main\settings.gradle.kts

```
// Bloco de configuração global para o gerenciamento de plugins.
// Define como e de onde o Gradle deve encontrar e resolver os plugins necessários para o projeto.
pluginManagement {
    // Bloco que declara os repositórios onde o Gradle procurará por plugins.
    repositories {
        // Declaração do repositório Maven do Google.
        // Este é fundamental para projetos Android, pois hospeda plugins como o Android Gradle Plugin (AGP).
        google {
            // Bloco 'content' permite uma filtragem mais granular dos artefatos que este repositório pode fornecer.
            // É uma medida de segurança e desempenho para evitar que o repositório seja consultado por artefatos que ele certamente não contém.
            content {
                // Inclui grupos de artefatos cujos nomes correspondem à expressão regular "com.android.*".
                // Isso garante que plugins relacionados ao Android (ex: com.android.application, com.android.library) sejam resolvidos aqui.
                includeGroupByRegex("com\\.android.*")
                // Inclui grupos de artefatos cujos nomes correspondem à expressão regular "com.google.*".
                // Isso cobre plugins fornecidos diretamente pelo Google, não necessariamente específicos do Android.
                includeGroupByRegex("com\\.google.*")
                // Inclui grupos de artefatos cujos nomes correspondem à expressão regular "androidx.*".
                // Abrange plugins relacionados às bibliotecas AndroidX.
                includeGroupByRegex("androidx.*")
            }
        }
        // Declaração do repositório Maven Central.
        // É um dos maiores repositórios de artefatos Java e hospeda uma vasta gama de plugins e bibliotecas.
        mavenCentral()
        // Declaração do Gradle Plugin Portal.
        // É o repositório oficial para a maioria dos plugins do Gradle, onde desenvolvedores publicam seus plugins.
        gradlePluginPortal()
    }
}
// Bloco de configuração global para o gerenciamento de dependências (bibliotecas, não plugins).
// Define como e de onde o Gradle deve encontrar e resolver as dependências para *todos* os subprojetos.
dependencyResolutionManagement {
    // Configuração do modo de repositórios para o projeto.
    // 'RepositoriesMode.FAIL_ON_PROJECT_REPOS' é uma configuração de segurança e padronização crítica.
    // Ela força que todos os subprojetos usem *apenas* os repositórios definidos neste bloco 'dependencyResolutionManagement'.
    // Se um subprojeto tentar declarar seus próprios repositórios, a build falhará.
    // Isso evita problemas de inconsistência, desempenho e segurança.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // Bloco que declara os repositórios onde o Gradle procurará por dependências de bibliotecas.
    repositories {
        // Atalho para o repositório Maven do Google.
        // Necessário para dependências Android e outras bibliotecas do Google.
        google()
        // Atalho para o repositório Maven Central.
        // Essencial para a maioria das bibliotecas Java de terceiros.
        mavenCentral()
    }
}

// Define o nome do projeto raiz.
// Este nome é usado em vários lugares, como o nome da pasta de build, ou ao referenciar o projeto em sistemas CI/CD.
rootProject.name = "TempoDica"
// Inclui o subprojeto chamado "app" na build.
// Isso significa que o Gradle procurará uma pasta chamada 'app' no diretório raiz do projeto,
// e que esta pasta conterá seu próprio arquivo 'build.gradle' para definir suas configurações específicas.
include(":app")
```

### TempoDica-main\app\build.gradle.kts

```gradle
// Define e aplica os plugins do Gradle necessários para o módulo Android.
plugins {
    // Aplica o plugin de aplicação Android, essencial para construir um APK.
    // 'libs.plugins.android.application' refere-se a uma maneira centralizada
    // de gerenciar versões de plugins via o arquivo 'libs.versions.toml' (TOML).
    alias(libs.plugins.android.application)
    // Aplica o plugin Kotlin Android, necessário para compilar código Kotlin no projeto.
    alias(libs.plugins.kotlin.android)
    // Aplica o plugin do compilador Compose, que integra o Jetpack Compose ao processo de build.
    alias(libs.plugins.compose.compiler)
}

// Bloco de configuração específico para o plugin Android.
android {
    // Define o namespace do pacote Java para as classes geradas (como a classe R).
    // É importante para evitar conflitos de nome e para a organização do código.
    namespace = "com.example.tempodica"
    // Define a versão do SDK do Android para compilação.
    // O projeto será compilado usando as APIs desta versão.
    compileSdk = 34

    // Bloco para configurar as definições padrão para todas as variantes de build.
    defaultConfig {
        // Define o ID único da aplicação. Este é o identificador único do seu app na Google Play Store.
        applicationId = "com.example.tempodica"
        // Define a versão mínima do SDK do Android que o aplicativo suporta.
        // O aplicativo não poderá ser instalado em dispositivos com uma versão de SDK inferior.
        minSdk = 26
        // Define a versão do SDK do Android que o aplicativo é projetado para rodar.
        // É importante que seja a versão mais recente para garantir o comportamento correto.
        targetSdk = 34
        // Define o código de versão da aplicação (um inteiro). Usado para controle de versões internas.
        versionCode = 1
        // Define o nome da versão da aplicação (uma string). Exibido aos usuários.
        versionName = "1.0"

        // Define o executor de testes de instrumentação padrão para testes Android.
        // Essencial para rodar testes na UI ou que precisam de um dispositivo/emulador.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Bloco de configuração para Drawables Vetoriais.
        vectorDrawables {
            // Indica que deve ser usada a biblioteca de suporte para drawables vetoriais.
            // Isso garante compatibilidade com versões mais antigas do Android.
            useSupportLibrary = true
        }
    }

    // Bloco para configurar diferentes tipos de build (por exemplo, 'release' e 'debug').
    buildTypes {
        // Configurações específicas para o tipo de build 'release'.
        release {
            // Desabilita a minificação de código (como Proguard/R8) para o build de release.
            // Em apps de produção, geralmente é 'true' para otimizar e ofuscar o código.
            isMinifyEnabled = false
            // Define os arquivos de regras do Proguard/R8 a serem aplicados.
            // O primeiro é um conjunto de regras padrão do Android, o segundo é um arquivo personalizado.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // Bloco para configurar opções de compilação Java.
    compileOptions {
        // Define a versão da linguagem Java usada para o código fonte.
        sourceCompatibility = JavaVersion.VERSION_1_8
        // Define a versão do bytecode Java que o compilador deve gerar.
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // Bloco para configurar opções de compilação Kotlin.
    kotlinOptions {
        // Define a versão da JVM para a qual o código Kotlin será compilado.
        jvmTarget = "1.8"
    }
    // Bloco para ativar ou desativar recursos de build específicos do Android.
    buildFeatures {
        // Ativa o suporte ao Jetpack Compose neste módulo.
        compose = true
    }
    // Bloco para configurar opções específicas do Jetpack Compose.
    composeOptions {
        // Define a versão da extensão do compilador Kotlin para o Compose.
        // É crucial para garantir a compatibilidade entre o Kotlin e as APIs do Compose.
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    // Bloco para configurar opções de empacotamento do APK/AAB.
    packaging {
        // Bloco para configurar o tratamento de recursos durante o empacotamento.
        resources {
            // Adiciona padrões de arquivos a serem excluídos do pacote final (APK/AAB).
            // Isso evita a inclusão de arquivos de licença duplicados ou desnecessários
            // que podem causar problemas em tempo de execução ou warnings de build.
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Bloco para declarar todas as dependências do projeto.
dependencies {
    // Implementação da biblioteca principal KTX do Android, que fornece extensões Kotlin para APIs existentes.
    implementation("androidx.core:core-ktx:1.12.0")
    // Implementação da biblioteca Lifecycle Runtime KTX, que gerencia o ciclo de vida de componentes Android.
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    // Implementação da biblioteca Activity Compose, necessária para usar Jetpack Compose em Activities.
    implementation("androidx.activity:activity-compose:1.8.2")
    // Importa a "Bill of Materials" (BOM) para o Jetpack Compose.
    // Usar a BOM permite que você declare dependências do Compose sem especificar suas versões individuais,
    // garantindo que todas as bibliotecas do Compose usadas sejam compatíveis entre si.
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    // Dependência da interface de usuário base do Compose.
    implementation("androidx.compose.ui:ui")
    // Dependência para gráficos no Compose (como Canvas, Path, etc.).
    implementation("androidx.compose.ui:ui-graphics")
    // Dependência para ferramentas de pré-visualização de componentes Compose no Android Studio.
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Dependência para os componentes de Material Design 3 no Compose.
    implementation("androidx.compose.material3:material3")
    // Dependência para ícones estendidos do Material Design no Compose.
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    // Dependência para integrar ViewModel com Jetpack Compose, permitindo o uso de ViewModels.
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Dependência para a biblioteca de navegação com Jetpack Compose.
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Dependência para a biblioteca de Coroutines Kotlin específica para Android.
    // Facilita o gerenciamento de tarefas assíncronas.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Dependência principal do Retrofit, um cliente HTTP type-safe para Android e Java.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Dependência do conversor Gson para Retrofit, que permite a conversão automática entre JSON e objetos Kotlin/Java.
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Dependência para o interceptor de logging do OkHttp, útil para depurar requisições HTTP.
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Dependência para a biblioteca Material Design do Google (versão para Views tradicionais, mas pode ser usada para alguns utilitários).
    implementation("com.google.android.material:material:1.12.0")


    // Dependência para testes unitários locais (JUnit).
    testImplementation(libs.junit)
    // Dependência para testes de instrumentação Android (JUnit para Android).
    androidTestImplementation(libs.androidx.junit)
    // Dependência para testes de UI com Espresso.
    androidTestImplementation(libs.androidx.espresso.core)
    // Importa a BOM do Compose para testes de instrumentação, garantindo compatibilidade de versões.
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    // Dependência para testes de UI com Compose no JUnit 4.
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Dependência para ferramentas de UI do Compose específicas para depuração (como inspeção de layout).
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Dependência para o manifesto de teste de UI do Compose, necessário para rodar testes de UI em debug.
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\MainActivity.kt

```kotlin
package com.example.tempodica // Declara o pacote Java/Kotlin para a organização do código. É o identificador único para este arquivo.

// Importações de classes e funções necessárias para a MainActivity.
import android.os.Bundle // Importa a classe Bundle, usada para passar dados entre atividades e salvar/restaurar o estado.
import androidx.activity.ComponentActivity // Importa a classe base ComponentActivity, uma atividade otimizada para Jetpack Compose.
import androidx.activity.compose.setContent // Importa a função setContent, que define o conteúdo da UI do Compose para a atividade.
import androidx.compose.foundation.layout.fillMaxSize // Importa o modificador fillMaxSize, usado para fazer um Composable preencher todo o espaço disponível.
import androidx.compose.material3.MaterialTheme // Importa MaterialTheme, que fornece acesso ao sistema de design Material 3 (cores, tipografia, formas).
import androidx.compose.material3.Surface // Importa Surface, um Composable de Material Design que aplica cor de fundo, elevação e forma.
import androidx.compose.ui.Modifier // Importa a interface Modifier, usada para decorar ou aumentar Composables.
import androidx.core.view.WindowCompat // Importa WindowCompat, uma classe utilitária para interagir com as barras do sistema (status e navegação).
import com.example.tempodica.ui.navigation.AppNavigation // Importa AppNavigation, um Composable customizado que gerencia a navegação do aplicativo.
import com.example.tempodica.ui.theme.TempoDicaTheme // Importa TempoDicaTheme, um Composable customizado que define o tema Material Design do aplicativo.

/**
 * Activity principal do aplicativo.
 *
 * - Usa Jetpack Compose para a UI.
 * - Ativa edge-to-edge para que o tema gerencie as barras do sistema.
 */
class MainActivity : ComponentActivity() { // Declara a classe MainActivity, que herda de ComponentActivity, tornando-a o ponto de entrada principal do aplicativo.

    override fun onCreate(savedInstanceState: Bundle?) { // Sobrescreve o método onCreate, chamado quando a atividade é criada pela primeira vez.
        super.onCreate(savedInstanceState) // Chama a implementação do onCreate da classe pai (ComponentActivity), essencial para a inicialização da atividade.

        // Habilita layout edge-to-edge (barra de status/navigation controlada pelo tema).
        // Isso faz com que o conteúdo do aplicativo se estenda por trás das barras de status e navegação,
        // permitindo que o tema controle a aparência dessas barras.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent { // Define o conteúdo da interface do usuário da atividade usando Jetpack Compose.
            TempoDicaTheme { // Aplica o tema personalizado TempoDicaTheme a todo o conteúdo da UI dentro deste bloco.
                Surface( // Um Composable que serve como uma superfície Material Design, aplicando uma cor de fundo e preenchendo o espaço.
                    modifier = Modifier.fillMaxSize(), // Modificador que instrui o Surface a preencher toda a largura e altura disponíveis da tela.
                    color = MaterialTheme.colorScheme.background // Define a cor de fundo do Surface usando a cor de fundo definida no esquema de cores do tema Material 3.
                ) {
                    // O NavHost dentro do AppNavigation controla qual tela mostrar.
                    // Chama o Composable AppNavigation, que é responsável por configurar e gerenciar o grafo de navegação do aplicativo,
                    // decidindo qual tela (Composable) deve ser exibida no momento.
                    AppNavigation()
                }
            }
        }
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\data\model\CurrentWeather.kt

```kotlin
/**
 * Declaração do pacote onde esta classe está localizada.
 * Segue a convenção de pacotes para organizar classes relacionadas à camada de dados (data)
 * e especificamente ao modelo (model) de dados.
 */
package com.example.tempodica.data.model

/**
 * Importa a anotação `SerializedName` da biblioteca Gson.
 * Esta anotação é usada para mapear nomes de campos JSON (que podem ser diferentes)
 * para os nomes das propriedades da classe Kotlin, facilitando a desserialização de JSON.
 */
import com.google.gson.annotations.SerializedName

/**
 * Classe de dados (data class) que representa o clima atual retornado por uma API externa.
 * Uma `data class` em Kotlin é otimizada para armazenar dados e automaticamente gera
 * métodos úteis como `equals()`, `hashCode()`, `toString()`, `copy()`, e `componentN()`.
 *
 * Esta classe encapsula as principais informações do tempo para uma determinada localização.
 *
 * @property temperature Temperatura atual em graus Celsius. É um `Double` para permitir valores decimais.
 *                      O valor padrão é `0.0` caso não seja fornecido na desserialização.
 * @property windSpeed Velocidade do vento em quilômetros por hora (km/h). Também é um `Double`.
 *                     O valor padrão é `0.0` caso não seja fornecido.
 * @property weatherCode Código numérico inteiro que representa o tipo de condição climática (ex: céu limpo, nublado, chuva).
 *                       O valor padrão é `-1`, indicando um código desconhecido ou não inicializado.
 */
data class CurrentWeather(

    /**
     * Anotação `@SerializedName("temperature")` indica que a propriedade `temperature`
     * deve ser mapeada para o campo JSON chamado "temperature" durante a desserialização.
     * `val temperature: Double = 0.0` declara uma propriedade de somente leitura (valor)
     * do tipo `Double` com um valor padrão de `0.0`.
     */
    @SerializedName("temperature")
    val temperature: Double = 0.0,

    /**
     * Anotação `@SerializedName("windspeed")` indica que a propriedade `windSpeed`
     * deve ser mapeada para o campo JSON chamado "windspeed".
     * `val windSpeed: Double = 0.0` declara uma propriedade de somente leitura
     * do tipo `Double` com um valor padrão de `0.0`.
     */
    @SerializedName("windspeed")
    val windSpeed: Double = 0.0,

    /**
     * Anotação `@SerializedName("weathercode")` indica que a propriedade `weatherCode`
     * deve ser mapeada para o campo JSON chamado "weathercode".
     * `val weatherCode: Int = -1` declara uma propriedade de somente leitura
     * do tipo `Int` com um valor padrão de `-1`.
     */
    @SerializedName("weathercode")
    val weatherCode: Int = -1
) {
    /**
     * Propriedade computada (ou getter personalizado) que retorna uma descrição textual
     * amigável e legível do tipo de clima, baseada no `weatherCode` numérico.
     *
     * A palavra-chave `get()` define um getter personalizado para esta propriedade.
     * O valor desta propriedade é calculado dinamicamente sempre que é acessada,
     * utilizando uma expressão `when`.
     */
    val description: String
        /**
         * Bloco `when` é uma estrutura de controle de fluxo em Kotlin similar ao `switch`
         * em outras linguagens, mas muito mais poderosa. Ele avalia o valor de `weatherCode`
         * e retorna uma String correspondente.
         *
         * - `0`: Retorna "Céu limpo" para o código 0.
         * - `1, 2, 3`: Retorna "Parcialmente nublado" para os códigos 1, 2 ou 3.
         * - `in 45..48`: Retorna "Névoa" para qualquer código entre 45 e 48 (inclusive).
         * - `in 51..67`: Retorna "Chuva fraca a moderada" para códigos entre 51 e 67.
         * - `in 71..77`: Retorna "Neve" para códigos entre 71 e 77.
         * - `in 80..82`: Retorna "Pancadas de chuva" para códigos entre 80 e 82.
         * - `in 95..99`: Retorna "Tempestade" para códigos entre 95 e 99.
         * - `else`: É o caso padrão, executado se nenhum dos códigos anteriores corresponder.
         *           Retorna "Clima desconhecido" para qualquer outro `weatherCode`.
         *           É crucial para garantir que a propriedade `description` sempre retorne um valor.
         */
        get() = when (weatherCode) {
            0 -> "Céu limpo"
            1, 2, 3 -> "Parcialmente nublado"
            in 45..48 -> "Névoa"
            in 51..67 -> "Chuva fraca a moderada"
            in 71..77 -> "Neve"
            in 80..82 -> "Pancadas de chuva"
            in 95..99 -> "Tempestade"
            else -> "Clima desconhecido"
        }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\data\model\HourlyForecast.kt

```kotlin
package com.example.tempodica.data.model
// Declaração do pacote onde esta classe se encontra. 
// Isso organiza o código em uma estrutura de diretórios lógica, indicando que 'HourlyForecast'
// faz parte do módulo 'data.model' dentro do projeto 'com.example.tempodica'.
// O pacote 'data.model' sugere que esta classe é um modelo de dados (POJO/DTO)
// responsável por representar a estrutura de dados para a previsão do tempo.

import com.google.gson.annotations.SerializedName
// Importa a anotação 'SerializedName' da biblioteca Gson.
// Gson é uma biblioteca Java (comumente usada em Kotlin) para serializar e desserializar
// objetos Java (ou Kotlin) para e de JSON.
// 'SerializedName' é usada para especificar o nome do campo JSON que deve ser mapeado
// para uma propriedade específica em sua classe Kotlin, caso o nome do campo JSON
// seja diferente do nome da propriedade na classe.

/**
 * Representa a previsão horária contendo listas de horários, temperaturas e códigos de clima.
 *
 * Esta 'data class' é projetada para modelar a resposta de uma API que fornece dados
 * meteorológicos em uma granularidade horária. Ela agrupa diversas informações
 * relacionadas a um período de tempo contínuo (por exemplo, as próximas 24 horas),
 * onde cada índice nas listas 'time', 'temperatures' e 'weatherCodes' corresponde
 * à mesma hora.
 *
 * Em Kotlin, uma 'data class' é uma classe que tem como principal objetivo armazenar dados.
 * O compilador gera automaticamente funções úteis como `equals()`, `hashCode()`, `toString()`,
 * `componentN()` e `copy()`, que são essenciais para classes de dados, reduzindo o código boilerplate.
 */
data class HourlyForecast(
    /**
     * Lista de horários correspondentes às previsões.
     *
     * Cada elemento nesta lista representa um horário específico (por exemplo, "2023-10-27T10:00")
     * para o qual os dados de temperatura e código meteorológico subsequentes são válidos.
     * A ordem dos elementos nesta lista corresponde à ordem dos elementos nas listas
     * `temperatures` e `weatherCodes`.
     */
    val time: List<String>, // 'val' indica que 'time' é uma propriedade somente leitura (imutável)
                           // 'List<String>' significa que é uma lista de objetos do tipo String.

    /**
     * Lista de temperaturas por hora.
     *
     * Cada elemento nesta lista representa a temperatura prevista para o horário correspondente
     * na lista `time`.
     *
     * `@SerializedName("temperature_2m")`: Esta anotação indica que, ao desserializar JSON para
     * um objeto `HourlyForecast` (ou serializar de volta para JSON), a propriedade Kotlin
     * `temperatures` deve ser mapeada para um campo JSON chamado "temperature_2m".
     * Isso é útil quando o nome do campo JSON (snake_case) não segue as convenções de nomeação
     * de Kotlin (camelCase), ou quando o nome da propriedade Kotlin é mais descritivo no contexto do código.
     */
    @SerializedName("temperature_2m") // Mapeia o campo JSON "temperature_2m" para esta propriedade.
    val temperatures: List<Double>, // 'List<Double>' significa que é uma lista de números de ponto flutuante de precisão dupla.

    /**
     * Lista de códigos meteorológicos por hora.
     *
     * Cada elemento nesta lista representa um código numérico que descreve o tipo de condição
     * climática para o horário correspondente na lista `time`. Estes códigos geralmente
     * se referem a uma especificação externa que os traduz para descrições legíveis (ex: 0 = "Céu limpo").
     *
     * `@SerializedName("weathercode")`: Similar à anotação em `temperatures`, esta indica que
     * a propriedade Kotlin `weatherCodes` deve ser mapeada para um campo JSON chamado "weathercode".
     */
    @SerializedName("weathercode") // Mapeia o campo JSON "weathercode" para esta propriedade.
    val weatherCodes: List<Int> // 'List<Int>' significa que é uma lista de números inteiros.
)
```

### TempoDica-main\app\src\main\java\com\example\tempodica\data\model\WeatherResponse.kt

```kotlin
package com.example.tempodica.data.model // Declara o pacote onde esta classe está localizada.
                                      // É uma prática comum organizar classes de modelo (dados) em um pacote 'model' dentro de 'data'.

import com.google.gson.annotations.SerializedName // Importa a anotação SerializedName do Gson.
                                                  // Esta anotação é usada para mapear nomes de campos JSON para nomes de propriedades Kotlin,
                                                  // especialmente quando eles são diferentes ou para garantir compatibilidade.

/**
 * Representa a resposta completa da API contendo clima atual e previsão horária.
 *
 * Esta é uma 'data class' em Kotlin, o que significa que ela é primariamente usada para armazenar dados.
 * Data classes automaticamente fornecem implementações para métodos úteis como `equals()`, `hashCode()`, `toString()`, `copy()`, e `componentN()`,
 * facilitando a manipulação e comparação de objetos de dados.
 */
data class WeatherResponse(
    /**
     * Dados do clima atual.
     *
     * @SerializedName("current_weather")
     *   Esta anotação informa ao Gson (biblioteca de serialização/desserialização JSON)
     *   que a propriedade Kotlin `currentWeather` deve ser mapeada para o campo JSON chamado "current_weather".
     *   Isso é útil quando o nome do campo JSON difere da convenção de nomenclatura Kotlin (camelCase vs. snake_case, por exemplo).
     *
     * `val currentWeather: CurrentWeather`
     *   Declara uma propriedade somente leitura chamada `currentWeather` do tipo `CurrentWeather`.
     *   O tipo `CurrentWeather` (presumivelmente outra data class) irá encapsular os detalhes específicos do clima atual.
     */
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,

    /**
     * Dados da previsão horária.
     *
     * @SerializedName("hourly")
     *   Similarmente, esta anotação mapeia a propriedade Kotlin `hourly` para o campo JSON chamado "hourly".
     *   Neste caso, o nome do campo JSON e da propriedade Kotlin são iguais, mas a anotação ainda pode ser usada
     *   para clareza ou se houver uma chance futura de o nome do campo JSON mudar.
     *
     * `val hourly: HourlyForecast`
     *   Declara uma propriedade somente leitura chamada `hourly` do tipo `HourlyForecast`.
     *   O tipo `HourlyForecast` (também presumivelmente outra data class) irá conter uma lista ou estrutura
     *   para os dados de previsão do tempo hora a hora.
     */
    @SerializedName("hourly")
    val hourly: HourlyForecast
)
```

### TempoDica-main\app\src\main\java\com\example\tempodica\data\remote\RetrofitInstance.kt

```kotlin
package com.example.tempodica.data.remote
// Declara o pacote onde este arquivo Kotlin está localizado.
// Convenções de nomenclatura de pacotes geralmente refletem a estrutura de diretórios do projeto
// e categorizam os arquivos por sua função (neste caso, "data.remote" sugere que lida com
// dados obtidos de uma fonte remota, como uma API).

import retrofit2.Retrofit
// Importa a classe Retrofit da biblioteca Retrofit.
// Retrofit é uma biblioteca de cliente HTTP type-safe para Android e Java,
// que facilita a comunicação com APIs REST.

import retrofit2.converter.gson.GsonConverterFactory
// Importa a classe GsonConverterFactory.
// Esta classe é um "conversor" do Retrofit que permite que objetos Kotlin/Java sejam
// serializados para JSON (para envio) e deserializados de JSON para objetos Kotlin/Java (para recebimento),
// utilizando a biblioteca Gson.

/**
 * Objeto responsável por fornecer a instância única do Retrofit
 * usada para acessar a API de clima.
 *
 * `object` em Kotlin cria um singleton, garantindo que haverá apenas uma
 * única instância de `RetrofitInstance` em toda a aplicação. Isso é ideal
 * para recursos que precisam ser compartilhados globalmente e configurados
 * apenas uma vez, como o cliente Retrofit.
 */
object RetrofitInstance {

    // Define a URL base para a API que será acessada.
    // `private const val` significa que esta é uma constante em tempo de compilação,
    // acessível apenas dentro deste objeto e não pode ser reatribuída.
    private const val BASE_URL = "https://api.open-meteo.com/"

    /**
     * Instância do Retrofit criada de forma lazy para melhor desempenho.
     *
     * - `private val retrofit: Retrofit`: Declara uma propriedade privada chamada `retrofit`
     *   do tipo `Retrofit`.
     * - `by lazy { ... }`: A inicialização é "lazy" (preguiçosa), o que significa que
     *   a instância de `Retrofit` só será criada na primeira vez que for acessada.
     *   Isso economiza recursos, pois a configuração do Retrofit pode ser custosa
     *   e pode não ser necessária imediatamente ao iniciar a aplicação.
     * - `Retrofit.Builder()`: Inicia o processo de construção de uma nova instância do Retrofit.
     * - `.baseUrl(BASE_URL)`: Define a URL base para todas as chamadas de API feitas por esta
     *   instância do Retrofit.
     * - `.addConverterFactory(GsonConverterFactory.create())`: Adiciona um "conversor"
     *   que instrui o Retrofit a usar Gson para converter JSON de/para objetos Kotlin/Java.
     * - `.build()`: Finaliza a construção e retorna a instância configurada do Retrofit.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Serviço da API exposto como singleton.
     *
     * - `val api: WeatherApiService`: Declara uma propriedade pública e imutável chamada `api`
     *   do tipo `WeatherApiService`. `WeatherApiService` seria uma interface Kotlin
     *   que define os endpoints da API usando anotações do Retrofit (não mostrada aqui).
     * - `by lazy { ... }`: Assim como `retrofit`, o serviço da API também é inicializado
     *   de forma lazy, otimizando o uso de recursos.
     * - `retrofit.create(WeatherApiService::class.java)`: Usa a instância de Retrofit
     *   (que é inicializada na primeira vez que `api` ou `retrofit` são acessados) para
     *   criar uma implementação da interface `WeatherApiService`. Retrofit gera
     *   automaticamente o código necessário para realizar as chamadas de rede definidas
     *   na interface. Esta instância gerada é o que a aplicação usará para interagir
     *   com a API de clima.
     */
    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\data\remote\WeatherApiService.kt

```kotlin
// Declaração do pacote onde esta interface está localizada.
// Usualmente, 'com.example.tempodica' é o domínio base, 'data' indica que é uma camada de dados,
// e 'remote' especifica que lida com fontes de dados remotas (como APIs).
package com.example.tempodica.data.remote

// Importa a classe 'WeatherResponse' que representa o modelo de dados
// esperado como resposta da API de clima. Esta classe (ou data class em Kotlin)
// deve mapear a estrutura JSON retornada pela API.
import com.example.tempodica.data.model.WeatherResponse
// Importa a anotação 'GET' do Retrofit, utilizada para declarar métodos
// que realizam requisições HTTP GET.
import retrofit2.http.GET
// Importa a anotação 'Query' do Retrofit, utilizada para adicionar parâmetros
// de consulta (query parameters) à URL da requisição.
import retrofit2.http.Query

/**
 * Interface que define o serviço de API para interagir com a API de clima Open-Meteo.
 *
 * Esta interface é crucial para o Retrofit, uma biblioteca HTTP client para Android e Java,
 * que a utiliza para criar uma implementação de rede. Ela abstrai os detalhes da requisição HTTP,
 * permitindo que o desenvolvedor se concentre na definição dos endpoints e seus respectivos
 * modelos de dados.
 */
interface WeatherApiService {

    /**
     * Função suspensa (suspend fun) para obter o clima atual e a previsão horária
     * de uma determinada localização.
     *
     * A palavra-chave `suspend` indica que esta é uma função de corrotina e pode ser
     * pausada e retomada posteriormente, tornando-a ideal para operações assíncronas
     * como chamadas de rede sem bloquear a thread principal.
     *
     * @param latitude A latitude do local para o qual se deseja obter a previsão do tempo.
     *                 É um parâmetro de consulta ('query parameter') na URL da requisição,
     *                 identificado pela chave "latitude".
     * @param longitude A longitude do local. Também um parâmetro de consulta ('query parameter')
     *                  na URL, identificado pela chave "longitude".
     * @param currentWeather Um booleano que indica se os dados do clima atual devem ser
     *                       incluídos na resposta. Por padrão, é `true`, ou seja, o clima atual
     *                       é sempre solicitado a menos que explicitamente definido como `false`.
     *                       Parâmetro de consulta "current_weather".
     * @param hourly Uma string que especifica quais parâmetros da previsão horária devem
     *               ser retornados. Por padrão, solicita "temperature_2m" (temperatura a 2 metros)
     *               e "weathercode" (código do tempo). Múltiplos parâmetros são separados por vírgula.
     *               Parâmetro de consulta "hourly".
     * @return Retorna um objeto do tipo [WeatherResponse], que é o modelo de dados
     *         mapeado a partir da resposta JSON da API. Este objeto conterá todos os
     *         dados do clima atual e da previsão horária solicitados.
     */
    // A anotação @GET("v1/forecast") especifica que esta função realizará uma requisição HTTP GET
    // para o caminho relativo "v1/forecast". Este caminho será anexado à URL base
    // configurada para o Retrofit (ex: https://api.open-meteo.com/v1/forecast).
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        // @Query("latitude") anota o parâmetro 'latitude' como um parâmetro de consulta
        // na URL, com o nome "latitude". Ex: ?latitude=...
        @Query("latitude") latitude: Double,
        // @Query("longitude") anota o parâmetro 'longitude' como um parâmetro de consulta
        // na URL, com o nome "longitude". Ex: &longitude=...
        @Query("longitude") longitude: Double,
        // @Query("current_weather") anota o parâmetro 'currentWeather' como um parâmetro
        // de consulta "current_weather". O valor padrão `true` significa que, se não for
        // fornecido, a API receberá `current_weather=true`.
        @Query("current_weather") currentWeather: Boolean = true,
        // @Query("hourly") anota o parâmetro 'hourly' como um parâmetro de consulta "hourly".
        // O valor padrão "temperature_2m,weathercode" garante que esses dados específicos
        // da previsão horária sejam solicitados se nenhum outro valor for especificado.
        @Query("hourly") hourly: String = "temperature_2m,weathercode"
    ): WeatherResponse // O tipo de retorno indica que a resposta JSON da API será
                       // automaticamente convertida em um objeto WeatherResponse.
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\repository\WeatherRepository.kt

```kotlin
package com.example.tempodica.repository
// Declara o pacote ao qual esta classe pertence. No contexto de um projeto Android,
// isso organiza o código logicamente, indicando que este arquivo faz parte da camada de repositório,
// responsável pela lógica de acesso a dados.

import com.example.tempodica.data.remote.RetrofitInstance
// Importa o objeto RetrofitInstance. Presume-se que este objeto é responsável por configurar e
// fornecer uma instância do serviço de API (interface Retrofit) que será utilizada para fazer
// requisições de rede. Ele é tipicamente um Singleton para gerenciar a instância do Retrofit.

import com.example.tempodica.data.model.WeatherResponse
// Importa a classe de dados WeatherResponse. Esta classe é um modelo de dados (provavelmente
// uma 'data class' em Kotlin) que representa a estrutura esperada da resposta da API de clima.
// Ela é usada para mapear o JSON recebido da API em um objeto Kotlin tipado.

/**
 * [WeatherRepository] é uma classe de repositório que serve como um ponto de acesso único e
 * abstrato para dados relacionados ao clima.
 *
 * Em uma arquitetura como MVVM (Model-View-ViewModel), o repositório atua como uma camada
 * intermediária entre as fontes de dados (neste caso, uma API remota) e os ViewModels.
 * Ele encapsula a lógica de obtenção, armazenamento e cache de dados, isolando o restante
 * da aplicação dos detalhes de implementação da fonte de dados.
 */
class WeatherRepository {

    // Declara uma propriedade privada e imutável chamada 'api'.
    // Ela é inicializada com a instância do serviço de API fornecida por RetrofitInstance.api.
    // 'RetrofitInstance.api' deve ser uma interface que define as chamadas da API de clima,
    // anotada com Retrofit para especificar os endpoints e métodos HTTP.
    // O modificador 'private' garante que 'api' só pode ser acessado dentro desta classe WeatherRepository,
    // encapsulando ainda mais os detalhes da comunicação com a API.
    private val api = RetrofitInstance.api

    /**
     * [getWeatherForecast] é uma função suspensa responsável por requisitar a previsão do tempo
     * para coordenadas geográficas específicas.
     *
     * @param latitude A coordenada de latitude (tipo [Double]) para a qual se deseja obter a previsão.
     *                 Representa a posição norte-sul no globo.
     * @param longitude A coordenada de longitude (tipo [Double]) para a qual se deseja obter a previsão.
     *                  Representa a posição leste-oeste no globo.
     * @return Um objeto [WeatherResponse] que encapsula todos os dados da previsão do tempo
     *         retornados pela API para as coordenadas fornecidas.
     *
     * O modificador 'suspend' indica que esta é uma função de suspensão (suspending function)
     * no contexto de Kotlin Coroutines. Isso significa que ela pode ser pausada e retomada,
     * permitindo que operações de longa duração (como chamadas de rede) sejam executadas de
     * forma assíncrona sem bloquear a thread principal da aplicação, essencial para manter
     * a interface do usuário responsiva.
     */
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        // Esta linha faz a chamada real à API de clima.
        // Ela invoca o método 'getWeatherForecast' na instância 'api' (que é o serviço Retrofit).
        // Os parâmetros 'latitude' e 'longitude' são passados diretamente para a função da API.
        // A função 'api.getWeatherForecast' (que também é uma função suspensa, definida na interface do serviço)
        // executará a requisição de rede em segundo plano.
        // O resultado da requisição, que é mapeado automaticamente pelo Retrofit para um objeto WeatherResponse,
        // é então retornado por esta função. Este é o ponto onde a coroutine suspende até que a resposta da rede seja recebida.
        return api.getWeatherForecast(latitude, longitude)
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\navigation\AppNavigation.kt

```kotlin
package com.example.tempodica.ui.navigation

// Importa a anotação Composable do Jetpack Compose, indicando que uma função pode ser usada para construir UI.
import androidx.compose.runtime.Composable
// Importa NavHostController, a classe que gerencia o estado da navegação e permite navegar entre destinos.
import androidx.navigation.NavHostController
// Importa o Composable NavHost, que é o container para a árvore de navegação e associa um NavController.
import androidx.navigation.compose.NavHost
// Importa a função composable, usada para definir um destino na navegação.
import androidx.navigation.compose.composable
// Importa rememberNavController, uma função Composable que cria e lembra uma instância de NavHostController.
import androidx.navigation.compose.rememberNavController
// Importa a tela de detalhes, que é um Composable a ser exibido em uma das rotas.
import com.example.tempodica.ui.screens.DetailsScreen
// Importa a tela inicial, que é um Composable a ser exibido em uma das rotas.
import com.example.tempodica.ui.screens.HomeScreen

/**
 * Objeto que centraliza as rotas da aplicação.
 * Facilita manutenção e evita erros de digitação.
 *
 * Este objeto define constantes para cada rota da aplicação. Usar um objeto singleton para definir
 * as rotas é uma prática recomendada, pois garante que todas as referências a uma rota específica
 * usem a mesma string, prevenindo erros de digitação e facilitando a refatoração.
 */
object AppDestinations {
    /**
     * Constante que representa a rota da tela inicial (Home).
     * Esta string será usada como identificador único para navegar até a tela Home.
     */
    const val HOME = "home"
    /**
     * Constante que representa a rota da tela de detalhes.
     * Esta string será usada como identificador único para navegar até a tela de detalhes.
     */
    const val DETAILS = "details"
}

/**
 * Gerencia a navegação principal do aplicativo.
 *
 * Esta função Composable é o ponto de entrada principal para a definição do grafo de navegação
 * da aplicação. Ela configura o `NavHost` que será responsável por exibir as telas Composable
 * conforme a navegação acontece.
 *
 * @param navController Uma instância de `NavHostController` que gerencia o estado da navegação.
 *                      Por padrão, uma nova instância é criada e lembrada usando `rememberNavController()`
 *                      se nenhuma for fornecida, o que é comum para o controlador de navegação principal.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    /**
     * O `NavHost` é o componente central para a navegação no Jetpack Compose.
     * Ele associa um `NavHostController` a um grafo de navegação, definindo os destinos
     * Composable que podem ser alcançados.
     *
     * @param navController O controlador de navegação que este NavHost irá usar para gerenciar
     *                      a pilha de back e navegar entre os destinos.
     * @param startDestination A rota inicial do grafo de navegação. Quando o NavHost é configurado
     *                         pela primeira vez, ele navega automaticamente para este destino.
     *                         Aqui, a tela inicial (HOME) é definida como o ponto de partida.
     */
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ) {

        /**
         * Define um destino Composable dentro do `NavHost` para a tela inicial (Home).
         *
         * @param route A string identificadora única para este destino. Deve corresponder a uma
         *              das constantes definidas em `AppDestinations`.
         * @param content O conteúdo Composable a ser exibido quando este destino estiver ativo.
         *                Aqui, `HomeScreen` é o Composable que representa a UI da tela inicial.
         */
        composable(route = AppDestinations.HOME) {
            // Instancia a HomeScreen, passando um callback para quando a navegação para a tela de detalhes for solicitada.
            HomeScreen(
                // onNavigateToDetails é uma função lambda (callback) que a HomeScreen chama
                // quando deseja navegar para a tela de detalhes.
                onNavigateToDetails = {
                    // O `navController.navigate()` é usado para navegar para um novo destino.
                    // Ele adiciona o destino à pilha de back da navegação.
                    navController.navigate(AppDestinations.DETAILS)
                }
            )
        }

        /**
         * Define um destino Composable dentro do `NavHost` para a tela de detalhes.
         *
         * @param route A string identificadora única para este destino.
         * @param content O conteúdo Composable a ser exibido quando este destino estiver ativo.
         *                Aqui, `DetailsScreen` é o Composable que representa a UI da tela de detalhes.
         */
        composable(route = AppDestinations.DETAILS) {
            // Instancia a DetailsScreen, passando um callback para quando a navegação de volta for solicitada.
            DetailsScreen(
                // onNavigateUp é uma função lambda (callback) que a DetailsScreen chama
                // quando deseja navegar para trás, geralmente para a tela anterior na pilha.
                onNavigateUp = {
                    // O `navController.navigateUp()` tenta navegar para o destino anterior na pilha de back.
                    // Se não houver destino anterior, pode fechar a atividade.
                    navController.navigateUp()
                }
            )
        }
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\screens\DetailsScreen.kt

```
// Declara o pacote onde o arquivo Kotlin está localizado.
// `com.example.tempodica.ui.screens` indica que este arquivo faz parte da interface do usuário (UI)
// e especificamente da camada de telas (screens) do aplicativo `tempodica`.
package com.example.tempodica.ui.screens

// Importações de componentes e modificadores do Jetpack Compose para construir a interface do usuário.
import androidx.compose.foundation.layout.Arrangement // Para organizar o espaçamento entre itens em layouts.
import androidx.compose.foundation.layout.Box // Um contêiner básico para posicionamento de elementos.
import androidx.compose.foundation.layout.Column // Um layout que organiza seus filhos verticalmente.
import androidx.compose.foundation.layout.Spacer // Um componente para adicionar espaço vazio.
import androidx.compose.foundation.layout.fillMaxSize // Um modificador para preencher o tamanho máximo disponível.
import androidx.compose.foundation.layout.height // Um modificador para definir a altura de um componente.
import androidx.compose.foundation.layout.padding // Um modificador para adicionar preenchimento (espaço interno).
import androidx.compose.foundation.layout.size // Um modificador para definir o tamanho de um componente.
import androidx.compose.foundation.lazy.LazyRow // Um layout para exibir uma lista horizontalmente, otimizado para grandes quantidades de itens.
import androidx.compose.foundation.lazy.items // Uma função de escopo para `LazyRow` que renderiza itens de uma lista.
import androidx.compose.material.icons.Icons // Objeto que contém os ícones padrão do Material Design.
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Ícone de seta para trás (que se espelha automaticamente para layouts RTL).
import androidx.compose.material.icons.filled.WbCloudy // Ícone de tempo nublado.
import androidx.compose.material.icons.filled.WbSunny // Ícone de tempo ensolarado.
import androidx.compose.material.icons.filled.WaterDrop // Ícone de gota de água (para chuva/chuvisco).
import androidx.compose.material3.Card // Um componente de superfície do Material Design com elevação e cantos arredondados.
import androidx.compose.material3.CircularProgressIndicator // Um indicador de progresso giratório para carregar dados.
import androidx.compose.material3.Icon // Um componente para exibir ícones.
import androidx.compose.material3.IconButton // Um botão clicável que exibe um ícone.
import androidx.compose.material3.MaterialTheme // Fornece cores, tipografia e formas padrão do Material Design.
import androidx.compose.material3.Scaffold // Um layout que implementa a estrutura básica da tela do Material Design (TopAppBar, FloatingActionButton, etc.).
import androidx.compose.material3.Text // Um componente para exibir texto.
import androidx.compose.material3.TopAppBar // Uma barra superior da aplicação, para título e ações.
import androidx.compose.runtime.Composable // Anotação para funções que podem ser usadas como componentes de UI no Compose.
import androidx.compose.runtime.LaunchedEffect // Um efeito colateral que é lançado uma vez quando o componente entra na composição.
import androidx.compose.runtime.collectAsState // Um coletor para `Flow` que converte seus valores em `State` do Compose.
import androidx.compose.runtime.getValue // Um delegado de propriedade para coletar o valor de um `State`.
import androidx.compose.ui.Alignment // Um objeto que define diferentes alinhamentos.
import androidx.compose.ui.Modifier // Uma interface usada para modificar a aparência e o comportamento de componentes Compose.
import androidx.compose.ui.graphics.vector.ImageVector // Representa um ícone gráfico vetorial.
import androidx.compose.ui.semantics.contentDescription // Para adicionar uma descrição de conteúdo para acessibilidade.
import androidx.compose.ui.semantics.semantics // Um modificador para adicionar informações de semântica (acessibilidade).
import androidx.compose.ui.text.style.TextAlign // Um objeto para definir o alinhamento horizontal do texto.
import androidx.compose.ui.unit.dp // Unidade de medida para densidade de pixels independentes.
import androidx.lifecycle.viewmodel.compose.viewModel // Uma função auxiliar para obter uma instância de ViewModel no contexto do Compose.
import com.example.tempodica.ui.viewmodel.DetailsViewModel // O ViewModel específico para esta tela de detalhes.
import java.time.Instant // Uma representação de um ponto específico no tempo na linha do tempo.
import java.time.ZoneId // Um identificador para uma região de fuso horário.
import java.time.format.DateTimeFormatter // Uma classe para formatar e analisar datas e horas.
import kotlin.math.roundToInt // Uma função de extensão para arredondar um número para o inteiro mais próximo.

// Modelo UI-friendly interno:
// Uma `data class` privada para representar de forma mais limpa e específica os dados de previsão horária
// que serão exibidos na interface do usuário. Isso desacopla o modelo da API do modelo de visualização.
private data class HourlyItemUi(
    val timeLabel: String, // O horário formatado para exibição (ex: "HH:mm").
    val temperature: Int, // A temperatura arredondada para o inteiro mais próximo.
    val icon: ImageVector, // O ícone do Material Design correspondente à condição climática.
    val description: String // Uma breve descrição da condição climática.
)

// Um formatador de data e hora para exibir apenas a hora e os minutos (HH:mm).
// É declarado como `private val` para ser uma instância única e acessível apenas dentro deste arquivo.
private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

// Função auxiliar para converter uma string de tempo ISO 8601 em um rótulo de hora local (HH:mm).
// Aceita a string ISO e um `ZoneId` opcional, padronizando para o fuso horário do sistema se não for fornecido.
private fun isoToLocalTimeLabel(iso: String, zone: ZoneId = ZoneId.systemDefault()): String {
    // Bloco `try-catch` para lidar com possíveis erros de parsing.
    return try {
        // Tenta parsear a string ISO diretamente para um `Instant`.
        val instant = Instant.parse(iso)
        // Converte o `Instant` para um `ZonedDateTime` no fuso horário especificado,
        // pega a hora local e a formata usando `timeFormatter`.
        instant.atZone(zone).toLocalTime().format(timeFormatter)
    } catch (_: Exception) {
        // Se o primeiro parse falhar (ex: formato ISO sem 'Z'), tenta um segundo `try-catch`.
        try {
            // Adiciona 'Z' (Zulu time/UTC) à string se ela ainda não terminar com 'Z',
            // para auxiliar no parsing de algumas strings ISO que omitem o fuso horário.
            val withZ = if (iso.endsWith("Z")) iso else "${iso}Z"
            Instant.parse(withZ).atZone(zone).toLocalTime().format(timeFormatter)
        } catch (_: Exception) {
            // Se ambos os parses falharem, usa um fallback simples:
            // Tenta extrair os últimos 5 caracteres da string (ex: "10:30")
            // e verifica se contém um ":". Se sim, retorna; caso contrário, retorna a string original inteira.
            iso.takeLast(5).takeIf { it.contains(":") } ?: iso
        }
    }
}

// Função auxiliar que mapeia códigos de condição climática (inteiros) para
// um par de `ImageVector` (ícone) e `String` (descrição textual).
// Isso centraliza a lógica de mapeamento de dados brutos da API para elementos de UI.
private fun weatherInfoFor(code: Int): Pair<ImageVector, String> =
    when (code) { // Utiliza uma expressão `when` para fazer o mapeamento.
        0 -> Icons.Default.WbSunny to "Céu limpo" // Código 0: Céu limpo, ícone de sol.
        1, 2, 3 -> Icons.Default.WbCloudy to "Parcialmente nublado" // Códigos 1-3: Nublado, ícone de nuvem.
        45, 48 -> Icons.Default.WbCloudy to "Nevoeiro" // Códigos 45-48: Nevoeiro, ícone de nuvem.
        51, 53, 55 -> Icons.Default.WaterDrop to "Chuvisco" // Códigos 51-55: Chuvisco, ícone de gota d'água.
        61, 63, 65 -> Icons.Default.WaterDrop to "Chuva" // Códigos 61-65: Chuva, ícone de gota d'água.
        else -> Icons.Default.WbSunny to "Desconhecido" // Qualquer outro código: Desconhecido, fallback para ícone de sol.
    }

// Anotação para indicar que a função usa APIs experimentais do Material 3, especificamente `TopAppBar`.
// Isso suprime avisos de compilação sobre o uso de APIs que podem mudar no futuro.
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable // Declara que esta é uma função Composable, um bloco de construção da UI do Jetpack Compose.
fun DetailsScreen(
    onNavigateUp: () -> Unit, // Função lambda que será chamada quando o usuário quiser navegar de volta.
    // Injeta o `DetailsViewModel` na função Composable. `viewModel()` é um produtor de ViewModel
    // que garante que uma única instância do ViewModel seja usada e persista durante a vida útil da tela.
    detailsViewModel: DetailsViewModel = viewModel()
) {
    // Coleta o `uiState` do `detailsViewModel` como um `State` observável do Compose.
    // Qualquer mudança no `uiState` fará com que os Composables que o leem sejam recompostos.
    val uiState by detailsViewModel.uiState.collectAsState()

    // `LaunchedEffect` executa um bloco de código de efeito colateral uma única vez quando
    // o Composable entra na composição e o "key" (aqui `Unit`) não muda.
    // É usado aqui para iniciar a busca da previsão do tempo assim que a tela é carregada.
    LaunchedEffect(Unit) {
        // Chama a função `fetchWeatherForecast` do ViewModel com coordenadas de latitude e longitude fixas (São Paulo).
        detailsViewModel.fetchWeatherForecast(-23.5505, -46.6333)
    }

    // `Scaffold` fornece a estrutura básica do Material Design para a tela.
    Scaffold(
        // Define a barra superior da aplicação.
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") }, // Título exibido na barra superior.
                navigationIcon = { // Ícone de navegação na barra superior (geralmente um botão de voltar).
                    IconButton(onClick = onNavigateUp) { // Um botão clicável que executa `onNavigateUp`.
                        Icon(
                            // O ícone de seta para trás, que se ajusta automaticamente para layouts RTL.
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar" // Descrição para acessibilidade.
                        )
                    }
                }
            )
        }
    ) { innerPadding -> // O `innerPadding` é um `PaddingValues` que o `Scaffold` fornece para que o conteúdo não se sobreponha à `TopAppBar`.
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo o espaço disponível.
                .padding(innerPadding) // Aplica o preenchimento fornecido pelo `Scaffold`.
                .padding(16.dp), // Adiciona um preenchimento extra de 16dp em todos os lados.
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza os elementos filhos horizontalmente.
        ) {

            // Título para a seção de previsão horária.
            Text(
                text = "Clima por Hora",
                style = MaterialTheme.typography.titleLarge // Usa o estilo de tipografia de título grande do Material Theme.
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento vertical.

            // Bloco `when` para exibir diferentes estados da UI baseados em `uiState`.
            when {
                // Caso em que os dados estão sendo carregados.
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        // Exibe um indicador de progresso circular.
                        CircularProgressIndicator(
                            // Adiciona uma descrição para acessibilidade, informando que a previsão horária está sendo carregada.
                            modifier = Modifier.semantics { contentDescription = "Carregando previsão horária" }
                        )
                    }
                }

                // Caso em que os dados já foram carregados (ou houve um erro ou não há dados).
                else -> {
                    // Captura os valores de `errorMessage` e `weather` em variáveis locais.
                    // Isso é útil para o Kotlin realizar "smart cast" e permitir acesso seguro a propriedades
                    // dentro dos blocos `if` subsequentes sem repetição de `uiState?.`.
                    val errorMsg = uiState.errorMessage
                    val weather = uiState.weather

                    // Se houver uma mensagem de erro, exibe-a.
                    if (errorMsg != null) {
                        Text(
                            text = errorMsg, // Texto da mensagem de erro.
                            color = MaterialTheme.colorScheme.error, // Usa a cor de erro do tema.
                            textAlign = TextAlign.Center, // Centraliza o texto.
                            style = MaterialTheme.typography.bodyLarge, // Estilo de tipografia para corpo de texto grande.
                            // Descrição para acessibilidade, informando que há um erro e qual é a mensagem.
                            modifier = Modifier.semantics { contentDescription = "Erro: $errorMsg" }
                        )
                    } else if (weather != null) { // Se não há erro e os dados de clima estão disponíveis.
                        // Extrai as listas de tempo, temperatura e códigos climáticos da resposta da API.
                        val hourly = weather.hourly
                        val times = hourly.time
                        val temps = hourly.temperatures
                        val codes = hourly.weatherCodes

                        // Calcula o menor tamanho entre as três listas para garantir que não haja `IndexOutOfBoundsException`.
                        // Se alguma lista estiver vazia, `size` será 0.
                        val size = listOf(times.size, temps.size, codes.size).minOrNull() ?: 0
                        if (size <= 0) {
                            // Se não há dados horários válidos, exibe uma mensagem.
                            Text(
                                text = "Dados horários não disponíveis",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.semantics { contentDescription = "Dados horários não disponíveis" }
                            )
                        } else {
                            // Obtém o momento atual.
                            val now = Instant.now()
                            // Encontra o índice do primeiro item de previsão que seja igual ou posterior ao momento atual.
                            // Isso garante que a previsão comece a partir do horário atual ou do próximo horário disponível.
                            // Se nenhum for encontrado (ex: todos os horários são no passado), `startIndex` será 0.
                            val startIndex = (0 until size).indexOfFirst { i ->
                                try {
                                    Instant.parse(times[i]) >= now
                                } catch (_: Exception) {
                                    false // Ignora entradas de tempo inválidas.
                                }
                            }.let { if (it == -1) 0 else it }

                            // Define o índice final para mostrar as próximas 12 horas de previsão.
                            // `coerceAtMost(size)` garante que não exceda o limite dos dados disponíveis.
                            val end = (startIndex + 12).coerceAtMost(size)
                            // Mapeia os dados brutos da API para a lista de `HourlyItemUi` para exibição.
                            val items = (startIndex until end).map { i ->
                                val iso = times[i] // String de tempo ISO.
                                val temp = temps.getOrNull(i) ?: 0.0 // Temperatura, com fallback para 0.0.
                                val code = codes.getOrNull(i) ?: -1 // Código do clima, com fallback para -1.
                                val (icon, desc) = weatherInfoFor(code) // Obtém ícone e descrição do clima.
                                // Cria uma instância de `HourlyItemUi`.
                                HourlyItemUi(
                                    timeLabel = isoToLocalTimeLabel(iso), // Formata a hora para exibição.
                                    temperature = temp.roundToInt(), // Arredonda a temperatura para o inteiro mais próximo.
                                    icon = icon, // Ícone do clima.
                                    description = desc // Descrição do clima.
                                )
                            }

                            // Verifica se há itens para exibir após o filtro.
                            if (items.isEmpty()) {
                                Text(
                                    text = "Nenhuma previsão disponível",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.semantics { contentDescription = "Nenhuma previsão disponível" }
                                )
                            } else {
                                // `LazyRow` é um componente para exibir uma lista de itens horizontalmente,
                                // carregando e reciclando itens apenas quando eles estão visíveis, otimizando o desempenho.
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp), // Espaçamento de 12dp entre os cards.
                                    modifier = Modifier.semantics { contentDescription = "Previsão horária, deslize para ver mais" }
                                ) {
                                    // Adiciona os itens à `LazyRow`, usando a função `HourlyCard` para cada um.
                                    items(items) { it ->
                                        HourlyCard(it)
                                    }
                                }
                            }
                        }
                    } else { // Se não há erro nem dados de clima.
                        Text(
                            text = "Sem dados",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.semantics { contentDescription = "Sem dados disponíveis" }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Espaçamento vertical.

            // Título para a seção de hidratação.
            Text(
                text = "Hidratação Ideal",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento vertical.

            // Texto com a recomendação de hidratação.
            Text(
                text = "Com base no clima de hoje, recomendamos que você beba pelo menos 2,5 litros de água para se manter hidratado.",
                textAlign = TextAlign.Center, // Centraliza o texto.
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Recomendação de hidratação: ao menos 2,5 litros de água" }
            )
        }
    }
}

@Composable // Declara que esta é uma função Composable.
// Componente Composable que exibe um único item de previsão horária em um `Card`.
private fun HourlyCard(item: HourlyItemUi) {
    Card(
        modifier = Modifier.semantics {
            // Adiciona uma descrição de conteúdo detalhada para acessibilidade,
            // combinando o horário, temperatura e descrição do clima.
            contentDescription = "Previsão às ${item.timeLabel}: ${item.temperature}°C, ${item.description}"
        }
    ) {
        Column(
            modifier = Modifier.padding(12.dp), // Preenchimento interno de 12dp.
            horizontalAlignment = Alignment.CenterHorizontally, // Centraliza os elementos filhos horizontalmente.
            verticalArrangement = Arrangement.Center // Centraliza os elementos filhos verticalmente.
        ) {
            // Exibe o rótulo de tempo (HH:mm).
            Text(text = item.timeLabel, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp)) // Espaçamento vertical.
            // Exibe o ícone do clima.
            Icon(
                imageVector = item.icon, // O ícone vetorial.
                contentDescription = item.description, // Descrição para acessibilidade.
                modifier = Modifier.size(36.dp) // Define o tamanho do ícone.
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaçamento vertical.
            // Exibe a temperatura.
            Text(text = "${item.temperature}°C", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\screens\HomeScreen.kt

```kotlin
package com.example.tempodica.ui.screens

import android.widget.Toast // Importa a classe Toast do Android para exibir mensagens curtas.
import androidx.compose.foundation.layout.* // Importa componentes de layout da Compose, como Column, Row, Box, Spacer, etc.
import androidx.compose.material3.* // Importa componentes do Material Design 3, como Scaffold, Text, Button, Card, Snackbar, TopAppBar, CircularProgressIndicator, Surface.
import androidx.compose.runtime.* // Importa funções essenciais do runtime da Compose, como @Composable, remember, LaunchedEffect, collectAsState, by.
import androidx.compose.ui.Alignment // Importa a classe Alignment para alinhar o conteúdo dentro de layouts.
import androidx.compose.ui.Modifier // Importa a classe Modifier, usada para modificar o comportamento e a aparência dos elementos da UI.
import androidx.compose.ui.platform.LocalConfiguration // Permite acessar a configuração do dispositivo local (e.g., largura da tela).
import androidx.compose.ui.platform.LocalContext // Permite acessar o Contexto Android atual.
import androidx.compose.ui.semantics.contentDescription // Importa para adicionar descrições de conteúdo para acessibilidade.
import androidx.compose.ui.semantics.semantics // Importa o modificador semantics para adicionar propriedades de acessibilidade.
import androidx.compose.ui.text.style.TextAlign // Importa TextAlign para configurar o alinhamento de texto.
import androidx.compose.ui.unit.dp // Unidade de medida para densidade de pixels independentes, recomendada para dimensões de UI.
import androidx.lifecycle.viewmodel.compose.viewModel // Importa a função para integrar ViewModels com Compose.
import com.example.tempodica.ui.viewmodel.UiEvent // Importa a classe selada que define eventos de UI de uso único.
import com.example.tempodica.ui.viewmodel.WeatherViewModel // Importa o ViewModel responsável pela lógica de negócios e estado da UI.
import kotlinx.coroutines.flow.collectLatest // Importa o operador collectLatest para coletar os fluxos (Flows) de forma eficiente.

/**
 * Anotação que permite o uso de APIs experimentais do Material Design 3.
 * É comum ao usar novos componentes ou funcionalidades ainda em desenvolvimento.
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
/**
 * `HomeScreen` é a tela principal da aplicação, exibindo informações meteorológicas.
 * É uma função `@Composable`, o que significa que ela descreve uma parte da interface do usuário.
 *
 * @param viewModel Uma instância de `WeatherViewModel`, fornecida por injeção de dependência (via `viewModel()`)
 *                  para gerenciar o estado da UI e a lógica de negócios. Por padrão, ele obtém o ViewModel
 *                  do escopo atual da composição.
 * @param onNavigateToDetails Uma função lambda que será chamada quando o usuário desejar navegar para
 *                            a tela de detalhes. É um padrão comum para que a navegação seja gerenciada
 *                            por um componente superior (e.g., um NavHost).
 */
fun HomeScreen(
    viewModel: WeatherViewModel = viewModel(),
    onNavigateToDetails: () -> Unit
) {
    /**
     * `uiState` é coletado do `viewModel.uiState` (um `StateFlow`) como um `State` da Compose.
     * O `by` permite que acessemos as propriedades de `uiState` diretamente (e.g., `uiState.isLoading`)
     * sem precisar usar `.value`. Qualquer mudança no `uiState` causará uma recomposição
     * das partes da UI que o leem.
     */
    val uiState by viewModel.uiState.collectAsState()
    /**
     * `snackbarHostState` é um objeto que gerencia o estado e a exibição de `Snackbar`s.
     * `remember` garante que esta instância seja preservada entre as recomposições.
     */
    val snackbarHostState = remember { SnackbarHostState() }
    /**
     * `context` obtém o `Context` Android atual da composição.
     * `LocalContext.current` é um `CompositionLocal` que fornece acesso a dados que
     * são "locais" à composição, mas que podem ser lidos por qualquer Composable filho.
     */
    val context = LocalContext.current

    /**
     * `LaunchedEffect` é um Composable de efeito colateral que executa um bloco de suspensão
     * quando suas chaves (neste caso, `viewModel`) mudam e quando o Composable entra na composição.
     * É ideal para iniciar operações assíncronas que devem ser canceladas e reiniciadas
     * se as chaves mudarem ou se o Composable sair da composição.
     * Aqui, ele é usado para coletar eventos de uso único (`UiEvent`) do ViewModel.
     */
    LaunchedEffect(viewModel) {
        /**
         * `viewModel.uiEvent.collectLatest` coleta os eventos emitidos pelo `SharedFlow` `uiEvent`
         * do ViewModel. `collectLatest` garante que, se um novo evento for emitido enquanto o anterior
         * ainda está sendo processado, o processamento anterior é cancelado e o novo é iniciado.
         * Isso é útil para eventos de uso único como mostrar uma mensagem.
         */
        viewModel.uiEvent.collectLatest { event ->
            // Usa uma expressão `when` para lidar com diferentes tipos de `UiEvent`.
            when (event) {
                is UiEvent.ShowToast -> {
                    /**
                     * Tenta mostrar um `Snackbar` usando o `snackbarHostState`.
                     * `showSnackbar` é uma função de suspensão que retorna um `SnackbarResult`
                     * indicando como o Snackbar foi dispensado.
                     */
                    val result = snackbarHostState.showSnackbar(event.message)
                    /**
                     * Se o `Snackbar` foi dispensado (por exemplo, deslizado para fora),
                     * há uma chance de o usuário não ter visto a mensagem. Como fallback,
                     * exibimos um `Toast` tradicional do Android para garantir que a mensagem seja entregue.
                     */
                    if (result == SnackbarResult.Dismissed) {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * `Scaffold` é um componente do Material Design que fornece uma estrutura básica para telas,
     * permitindo a colocação de barras superiores, barras inferiores, SnackBarHosts, etc.
     * Ele lida com o preenchimento (padding) para evitar que o conteúdo se sobreponha a esses elementos.
     */
    Scaffold(
        /**
         * Define o `SnackbarHost` para esta tela, que é o local onde os `Snackbar`s serão exibidos.
         * Ele usa o `snackbarHostState` para controlar a exibição das mensagens.
         */
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        /**
         * Define a barra superior da tela.
         * `CenterAlignedTopAppBar` é um `TopAppBar` que centraliza o título.
         */
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Tempo Agora") })
        }
    ) { innerPadding -> // `innerPadding` é um `PaddingValues` fornecido pelo Scaffold para o conteúdo.
        /**
         * `Surface` é um componente do Material Design que representa uma superfície com cor e elevação.
         * Aqui, ele atua como o contêiner principal para o conteúdo da tela.
         */
        Surface(
            modifier = Modifier
                .fillMaxSize() // Faz com que a Surface ocupe o máximo de espaço disponível.
                .padding(innerPadding), // Aplica o preenchimento fornecido pelo Scaffold para evitar sobreposição.
            color = MaterialTheme.colorScheme.background // Define a cor de fundo da Surface usando o esquema de cores do tema.
        ) {
            // Usamos LocalConfiguration para medir largura da tela (evita BoxWithConstraints warning)
            /**
             * `LocalConfiguration.current` obtém a configuração atual do dispositivo.
             */
            val configuration = LocalConfiguration.current
            /**
             * `screenWidthDp` obtém a largura da tela em unidades de densidade de pixels independentes.
             */
            val screenWidthDp = configuration.screenWidthDp
            /**
             * `isCompact` é uma flag booleana que determina se a tela é considerada "compacta" (e.g., celular em retrato)
             * com base em um breakpoint comum de 600dp. Usado para adaptar o layout e a tipografia.
             */
            val isCompact = screenWidthDp < 600

            // Capture local copies to avoid smart-cast issues later
            /**
             * Cria cópias locais e imutáveis das propriedades do `uiState`. Isso é uma boa prática
             * em Kotlin para garantir que o compilador possa fazer smart-casts com segurança,
             * especialmente em blocos `when` onde as propriedades podem ser verificadas para null.
             * Isso evita problemas se o `uiState` subjacente mudar entre verificações.
             */
            val errorMsg = uiState.errorMessage
            val temperature = uiState.temperature
            val description = uiState.weatherDescription
            val suggestion = uiState.suggestion

            /**
             * Uma expressão `when` para alternar entre diferentes estados da UI:
             * carregando, erro ou exibindo o conteúdo principal.
             */
            when {
                uiState.isLoading -> { // Se o estado de carregamento é verdadeiro.
                    /**
                     * `Box` é um layout simples que pode empilhar ou centralizar seu conteúdo.
                     */
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // Ocupa todo o espaço disponível.
                            /**
                             * `semantics` é usado para adicionar informações de acessibilidade.
                             * `contentDescription` fornece um texto descritivo para leitores de tela.
                             */
                            .semantics { contentDescription = "Carregando dados do tempo" },
                        contentAlignment = Alignment.Center // Centraliza o conteúdo (o indicador de progresso) dentro do Box.
                    ) {
                        /**
                         * `CircularProgressIndicator` exibe um spinner de carregamento circular,
                         * indicando que uma operação está em andamento.
                         */
                        CircularProgressIndicator()
                    }
                }

                errorMsg != null -> { // Se houver uma mensagem de erro.
                    /**
                     * `Column` organiza seus filhos verticalmente.
                     */
                    Column(
                        modifier = Modifier
                            .fillMaxSize() // Ocupa todo o espaço.
                            .padding(16.dp) // Adiciona preenchimento de 16dp ao redor do Column.
                            /**
                             * Adiciona uma descrição de conteúdo para acessibilidade, incluindo a mensagem de erro.
                             */
                            .semantics { contentDescription = "Erro ao carregar: $errorMsg" },
                        verticalArrangement = Arrangement.Center, // Centraliza os itens verticalmente.
                        horizontalAlignment = Alignment.CenterHorizontally // Centraliza os itens horizontalmente.
                    ) {
                        /**
                         * `Text` exibe a mensagem de erro.
                         */
                        Text(
                            text = errorMsg, // Texto da mensagem de erro.
                            color = MaterialTheme.colorScheme.error, // Usa a cor de erro definida no tema.
                            textAlign = TextAlign.Center, // Centraliza o texto.
                            /**
                             * Ajusta o estilo do texto com base na largura da tela (compacta ou não).
                             */
                            style = if (isCompact) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp)) // Adiciona um espaço vertical de 12dp.
                        /**
                         * `Button` para tentar novamente.
                         * Ao ser clicado, ele chama a função `fetchWeather()` no ViewModel para recarregar os dados.
                         */
                        Button(onClick = { viewModel.fetchWeather() }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                else -> { // Se não estiver carregando e não houver erro, exibe o conteúdo do tempo.
                    /**
                     * Chama o Composable `WeatherContent` para exibir os detalhes do tempo.
                     * Passa os dados necessários e o callback de navegação.
                     */
                    WeatherContent(
                        temperature = temperature,
                        description = description,
                        suggestion = suggestion,
                        onNavigateToDetails = onNavigateToDetails,
                        isCompact = isCompact
                    )
                }
            }
        }
    }
}

@Composable
/**
 * `WeatherContent` é um Composable que exibe as informações detalhadas do tempo e uma sugestão.
 * É um componente de UI puro, recebendo todos os dados e callbacks como parâmetros.
 *
 * @param temperature A string formatada da temperatura.
 * @param description A descrição atual do clima (e.g., "Ensolarado", "Chuvoso").
 * @param suggestion Uma dica de vestuário ou atividade com base no clima.
 * @param onNavigateToDetails Uma função lambda para navegar para a tela de detalhes.
 * @param isCompact Um booleano indicando se a tela é compacta, para ajustes responsivos.
 */
fun WeatherContent(
    temperature: String,
    description: String,
    suggestion: String,
    onNavigateToDetails: () -> Unit,
    isCompact: Boolean
) {
    /**
     * `Column` organiza seus filhos verticalmente, centralizando-os.
     */
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo o espaço disponível.
            .padding(16.dp) // Adiciona preenchimento de 16dp ao redor do Column.
            /**
             * Adiciona uma descrição de conteúdo abrangente para acessibilidade,
             * combinando todas as informações exibidas.
             */
            .semantics {
                contentDescription =
                    "Temperatura atual: $temperature. Condição: $description. Dica: $suggestion"
            },
        verticalArrangement = Arrangement.Center, // Centraliza os itens verticalmente.
        horizontalAlignment = Alignment.CenterHorizontally // Centraliza os itens horizontalmente.
    ) {
        /**
         * `Text` para o título "Tempo".
         * O estilo da tipografia é ajustado com base na flag `isCompact`.
         */
        Text(
            text = "Tempo",
            style = if (isCompact) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary // Usa a cor primária do tema.
        )

        Spacer(modifier = Modifier.height(24.dp)) // Adiciona um espaço vertical.

        /**
         * `Text` para exibir a temperatura.
         * O estilo da tipografia é ajustado para ser grande e impactante.
         */
        Text(
            text = temperature,
            style = if (isCompact) MaterialTheme.typography.displaySmall else MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(8.dp)) // Adiciona um espaço vertical.

        /**
         * `Text` para exibir a descrição do tempo.
         * O estilo da tipografia é ajustado.
         */
        Text(
            text = description,
            style = if (isCompact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp)) // Adiciona um espaço vertical.

        /**
         * `Text` para o subtítulo "Dica para hoje".
         * O estilo da tipografia é ajustado e usa a cor primária do tema.
         */
        Text(
            text = "Dica para hoje",
            style = if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp)) // Adiciona um espaço vertical.

        /**
         * `Card` é um componente do Material Design que exibe conteúdo em uma superfície elevada.
         * Usado para destacar a sugestão do dia.
         */
        Card(
            modifier = Modifier
                .fillMaxWidth() // Faz com que o Card preencha a largura máxima disponível.
                .padding(horizontal = 4.dp), // Adiciona um pequeno preenchimento horizontal.
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Adiciona uma elevação (sombra) ao Card.
        ) {
            /**
             * `Text` para exibir a sugestão do dia dentro do Card.
             */
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyLarge, // Usa o estilo de corpo de texto grande do tema.
                modifier = Modifier
                    .padding(16.dp) // Adiciona preenchimento interno ao texto.
                    /**
                     * Adiciona uma descrição de conteúdo específica para a sugestão.
                     */
                    .semantics { contentDescription = "Sugestão do dia: $suggestion" },
                textAlign = TextAlign.Center // Centraliza o texto da sugestão.
            )
        }

        Spacer(modifier = Modifier.height(20.dp)) // Adiciona um espaço vertical.

        /**
         * `Button` para navegar para mais detalhes.
         */
        Button(
            onClick = onNavigateToDetails, // Chama a função lambda `onNavigateToDetails` quando o botão é clicado.
            modifier = Modifier
                .widthIn(min = 160.dp) // Define uma largura mínima para o botão.
                /**
                 * Adiciona uma descrição de conteúdo para acessibilidade.
                 */
                .semantics { contentDescription = "Ver mais detalhes" }
        ) {
            Text("Ver mais detalhes") // Texto exibido no botão.
        }
    }
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\theme\Color.kt

```kotlin
// Declaração do pacote onde este arquivo Kotlin está localizado.
// Pacotes são usados para organizar classes e funções, prevenindo conflitos de nomes.
package com.example.tempodica.ui.theme

// Importa a classe 'Color' do pacote 'androidx.compose.ui.graphics'.
// A classe 'Color' é fundamental no Jetpack Compose para definir e manipular cores na interface do usuário.
import androidx.compose.ui.graphics.Color

/**
 * Cores claras usadas no tema Light.
 * Este bloco de documentação (KDoc) descreve o propósito das variáveis de cor que se seguem,
 * indicando que são para um tema de interface do usuário claro.
 */

// Declaração de uma constante (val) chamada Purple80.
// 'val' em Kotlin indica uma variável imutável (somente leitura).
// 'Color(0xFFD0BCFF)' cria uma instância da classe Color a partir de um valor hexadecimal ARGB (Alpha, Red, Green, Blue).
// 0xFF indica opacidade total, D0BCFF são os componentes de vermelho, verde e azul.
val Purple80 = Color(0xFFD0BCFF)

// Declaração da constante PurpleGrey80, uma cor cinza arroxeada clara.
// Segue o mesmo padrão de criação de cor hexadecimal ARGB.
val PurpleGrey80 = Color(0xFFCCC2DC)

// Declaração da constante Pink80, uma cor rosa clara.
// Segue o mesmo padrão de criação de cor hexadecimal ARGB.
val Pink80 = Color(0xFFEFB8C8)

/**
 * Cores escuras usadas no tema Dark.
 * Este bloco de documentação (KDoc) descreve o propósito das variáveis de cor que se seguem,
 * indicando que são para um tema de interface do usuário escuro.
 */

// Declaração da constante Purple40, uma cor roxa escura.
// 'val' novamente indica uma constante imutável.
// A cor é definida por seu valor hexadecimal ARGB.
// O número "40" e "80" no nome das variáveis de cor são frequentemente convenções para indicar
// a saturação ou claridade da cor em um sistema de design (como Material Design).
val Purple40 = Color(0xFF6650A4)

// Declaração da constante PurpleGrey40, uma cor cinza arroxeada escura.
// Segue o mesmo padrão de criação de cor hexadecimal ARGB.
val PurpleGrey40 = Color(0xFF625B71)

// Declaração da constante Pink40, uma cor rosa escura.
// Segue o mesmo padrão de criação de cor hexadecimal ARGB.
val Pink40 = Color(0xFF7D5260)
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\theme\Theme.kt

```kotlin
package com.example.tempodica.ui.theme

// Importa a classe Activity do pacote Android, necessária para acessar a janela do aplicativo.
import android.app.Activity
// Importa a classe Build do pacote Android, utilizada para verificar a versão do SDK do dispositivo.
import android.os.Build
// Importa a função isSystemInDarkTheme do Compose Foundation, que verifica se o sistema está no tema escuro.
import androidx.compose.foundation.isSystemInDarkTheme
// Importa o Composable MaterialTheme, o componente raiz para aplicar o tema Material Design 3.
import androidx.compose.material3.MaterialTheme
// Importa a função darkColorScheme, utilizada para criar um esquema de cores para o tema escuro.
import androidx.compose.material3.darkColorScheme
// Importa a função dynamicDarkColorScheme, que gera um esquema de cores escuras baseado no papel de parede (Android 12+).
import androidx.compose.material3.dynamicDarkColorScheme
// Importa a função dynamicLightColorScheme, que gera um esquema de cores claras baseado no papel de parede (Android 12+).
import androidx.compose.material3.dynamicLightColorScheme
// Importa a função lightColorScheme, utilizada para criar um esquema de cores para o tema claro.
import androidx.compose.material3.lightColorScheme
// Importa a anotação Composable, indicando que a função é uma função de UI do Jetpack Compose.
import androidx.compose.runtime.Composable
// Importa a função SideEffect, utilizada para executar efeitos colaterais (não relacionados à UI) dentro de um Composable.
import androidx.compose.runtime.SideEffect
// Importa a função de extensão toArgb, que converte uma cor Compose para um valor inteiro ARGB.
import androidx.compose.ui.graphics.toArgb
// Importa o LocalContext, um CompositionLocal que fornece o contexto Android atual.
import androidx.compose.ui.platform.LocalContext
// Importa o LocalView, um CompositionLocal que fornece a View Android subjacente para o Composable.
import androidx.compose.ui.platform.LocalView
// Importa a classe WindowCompat, que fornece compatibilidade para APIs de janela, incluindo controle de insets.
import androidx.core.view.WindowCompat

/**
 * Define o esquema de cores padrão para o tema escuro da aplicação.
 * As cores são obtidas de objetos de cor definidos em Colors.kt (Purple80, PurpleGrey80, Pink80).
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,      // Cor principal para o tema escuro.
    secondary = PurpleGrey80, // Cor secundária para o tema escuro.
    tertiary = Pink80         // Cor terciária para o tema escuro.
)

/**
 * Define o esquema de cores padrão para o tema claro da aplicação.
 * As cores são obtidas de objetos de cor definidos em Colors.kt (Purple40, PurpleGrey40, Pink40).
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,      // Cor principal para o tema claro.
    secondary = PurpleGrey40, // Cor secundária para o tema claro.
    tertiary = Pink40         // Cor terciária para o tema claro.
)

/**
 * `TempoDicaTheme` é a função Composable principal que encapsula o tema Material3 da aplicação.
 * Ela determina qual esquema de cores usar (dinâmico, claro ou escuro) e aplica-o aos elementos filhos.
 * Também gerencia a aparência das barras de status e navegação do sistema para corresponder ao tema.
 *
 * @param darkTheme Determina se o tema deve ser escuro. Por padrão, usa a configuração do sistema (`isSystemInDarkTheme()`).
 * @param dynamicColor Se verdadeiro, tenta usar cores dinâmicas no Android 12+ (Material You). Padrão é `true`.
 * @param content O conteúdo da UI que será aplicado com este tema.
 */
@Composable
fun TempoDicaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Verifica se o sistema está em modo escuro como padrão.
    dynamicColor: Boolean = true,              // Habilita cores dinâmicas por padrão.
    content: @Composable () -> Unit             // Bloco de conteúdo da UI a ser aplicado com o tema.
) {
    // Determina o esquema de cores a ser utilizado com base nas configurações e versão do Android.
    val colorScheme = when {
        // Se `dynamicColor` estiver habilitado e a versão do SDK for Android 12 (S) ou superior.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // Obtém o contexto Android atual, necessário para as funções de cores dinâmicas.
            val context = LocalContext.current
            // Se o tema for escuro, gera um esquema de cores dinâmico escuro; caso contrário, gera um claro.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Se `dynamicColor` não estiver habilitado ou a versão do SDK for anterior ao Android 12,
        // verifica se `darkTheme` está ativo e usa o esquema de cores escuro pré-definido.
        darkTheme -> DarkColorScheme
        // Caso contrário (tema claro e sem cores dinâmicas), usa o esquema de cores claro pré-definido.
        else -> LightColorScheme
    }

    // --- Seção para atualizar as cores da barra de status e navegação do sistema ---

    // Obtém a View Android subjacente para o Composable.
    val view = LocalView.current
    // Garante que o código de manipulação da janela só seja executado em tempo de execução real,
    // e não em ferramentas de pré-visualização ou no editor de layout.
    if (!view.isInEditMode) {
        // SideEffect é usado para executar código que não é Composable (efeitos colaterais),
        // como interagir com a janela do Android, de forma segura dentro do ciclo de vida de um Composable.
        SideEffect {
            // `useDarkIcons` determina se os ícones da barra de status/navegação devem ser escuros.
            // Ícones escuros são usados quando o tema é claro (para contraste em um fundo claro).
            val useDarkIcons = !darkTheme
            // `runCatching` é usado para encapsular operações que podem falhar (como cast para Activity),
            // evitando crashes e permitindo um tratamento de erro suave.
            runCatching {
                // Tenta converter o contexto da view para uma Activity e, se for bem-sucedido, acessa a janela.
                (view.context as? Activity)?.window?.let { window ->
                    // --- Barra de Status ---
                    // Define a cor de fundo da barra de status para a cor primária do esquema de cores atual.
                    window.statusBarColor = colorScheme.primary.toArgb()
                    // Controla a aparência dos ícones da barra de status (ícones claros vs. ícones escuros).
                    // `isAppearanceLightStatusBars = true` significa que os ícones são escuros (para fundo claro).
                    // `isAppearanceLightStatusBars = false` significa que os ícones são claros (para fundo escuro).
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useDarkIcons

                    // --- Barra de Navegação (opcional) ---
                    // Define a cor de fundo da barra de navegação para a cor de superfície do esquema de cores.
                    // Usar 'surface' pode proporcionar melhor contraste e consistência com o Material Design.
                    window.navigationBarColor = colorScheme.surface.toArgb()
                    // Controla a aparência dos ícones da barra de navegação, semelhante à barra de status.
                    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = useDarkIcons
                }
            }.onFailure {
                // Em caso de falha (por exemplo, se o contexto não for uma Activity), o erro é ignorado silenciosamente.
                // Isso evita crashes em ambientes onde a janela pode não estar disponível ou manipulável (e.g., testes).
            }
        }
    }

    // --- Aplicação do Tema Material Design ---

    // O Composable MaterialTheme aplica o esquema de cores, tipografia e formas aos componentes filhos.
    // Tudo dentro do `content` será renderizado usando as propriedades deste tema.
    MaterialTheme(
        colorScheme = colorScheme, // O esquema de cores determinado anteriormente (dinâmico, escuro ou claro).
        typography = Typography,   // A tipografia definida em Typography.kt.
        content = content          // O conteúdo da UI que receberá o tema.
    )
}
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\theme\Type.kt

```kotlin
package com.example.tempodica.ui.theme

import androidx.compose.material3.Typography // Importa a classe Typography do Material Design 3 para definir um conjunto de estilos de texto.
import androidx.compose.ui.text.TextStyle // Importa a classe TextStyle para definir propriedades como fonte, tamanho e peso.
import androidx.compose.ui.text.font.FontFamily // Importa a classe FontFamily para especificar a família da fonte.
import androidx.compose.ui.text.font.FontWeight // Importa a classe FontWeight para especificar o peso da fonte (ex: negrito, normal).
import androidx.compose.ui.unit.sp // Importa a extensão 'sp' (scaled pixels) para definir tamanhos de fonte de forma escalável.

/**
 * Tipografia base do app TempoDica usando Material 3.
 * Organizada e adequada para títulos, corpo e labels.
 *
 * Esta constante 'Typography' define um conjunto de estilos de texto que serão usados
 * consistentemente em toda a aplicação TempoDica, seguindo as diretrizes do Material 3.
 * Cada propriedade dentro de 'Typography' (como headlineLarge, bodyLarge, etc.)
 * corresponde a um papel específico de texto na interface do usuário.
 */
val Typography = Typography(

    /**
     * Define o estilo para títulos grandes e impactantes.
     * Exemplos de uso: Títulos principais de tela como "Tempo" ou "Dica para hoje".
     */
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão do sistema.
        fontWeight = FontWeight.Bold, // Define o peso da fonte como negrito, ideal para chamar atenção.
        fontSize = 28.sp, // Define o tamanho da fonte em 28 scaled pixels, grande para títulos.
        lineHeight = 34.sp // Define a altura da linha em 34 scaled pixels, proporcionando bom espaçamento vertical.
    ),

    /**
     * Define o estilo para títulos menores ou subtítulos importantes.
     * Exemplos de uso: Subtítulos em seções ou cards que precisam de destaque.
     */
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão.
        fontWeight = FontWeight.Medium, // Define o peso da fonte como médio, menos impactante que 'Bold' mas ainda com destaque.
        fontSize = 18.sp, // Define o tamanho da fonte em 18 scaled pixels, menor que 'headlineLarge'.
        lineHeight = 24.sp // Define a altura da linha em 24 scaled pixels.
    ),

    /**
     * Define o estilo para o corpo principal de texto do aplicativo.
     * Exemplos de uso: Textos de previsão do tempo, descrições longas, parágrafos.
     * É a base para a leitura de conteúdo.
     */
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão.
        fontWeight = FontWeight.Normal, // Define o peso da fonte como normal, ideal para legibilidade em grandes blocos de texto.
        fontSize = 16.sp, // Define o tamanho da fonte em 16 scaled pixels, um bom tamanho para leitura.
        lineHeight = 24.sp, // Define a altura da linha em 24 scaled pixels, ajudando na legibilidade.
        letterSpacing = 0.3.sp // Adiciona um leve espaçamento entre as letras para melhorar a leitura.
    ),

    /**
     * Define o estilo para o corpo secundário de texto, geralmente um pouco menor que 'bodyLarge'.
     * Exemplos de uso: Detalhes adicionais, informações complementares, legendas pequenas.
     */
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão.
        fontWeight = FontWeight.Normal, // Define o peso da fonte como normal.
        fontSize = 14.sp, // Define o tamanho da fonte em 14 scaled pixels, ligeiramente menor que 'bodyLarge'.
        lineHeight = 20.sp, // Define a altura da linha em 20 scaled pixels.
        letterSpacing = 0.2.sp // Adiciona um leve espaçamento entre as letras.
    ),

    /**
     * Define o estilo para títulos de componentes, como cards ou listas.
     * Exemplos de uso: Títulos dentro de cards de dica, cabeçalhos de itens em listas.
     * Geralmente mais proeminente que o 'body' text, mas menos que os 'headline' texts.
     */
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão.
        fontWeight = FontWeight.SemiBold, // Define o peso da fonte como semi-negrito, dando um destaque adequado para títulos de componentes.
        fontSize = 16.sp, // Define o tamanho da fonte em 16 scaled pixels.
        lineHeight = 22.sp // Define a altura da linha em 22 scaled pixels.
    ),

    /**
     * Define o estilo para labels de botões ou outros elementos interativos.
     * Exemplos de uso: Texto em botões como "Ver detalhes", "Salvar", "Atualizar".
     * A legibilidade e o destaque são importantes aqui.
     */
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default, // Usa a família de fonte padrão.
        fontWeight = FontWeight.Bold, // Define o peso da fonte como negrito, para garantir que o label do botão seja facilmente visível.
        fontSize = 14.sp, // Define o tamanho da fonte em 14 scaled pixels, um tamanho comum para botões.
        lineHeight = 20.sp // Define a altura da linha em 20 scaled pixels.
    )
)
```

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\viewmodel\DetailsViewModel.kt

package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.data.model.WeatherResponse
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val errorMessage: String? = null
)

class DetailsViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

// Função definida na linha 24
    fun fetchWeatherForecast(latitude: Double, longitude: Double) {
        _uiState.value = DetailsUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getWeatherForecast(latitude, longitude)

                _uiState.value = DetailsUiState(
                    isLoading = false,
                    weather = response
                )

            } catch (e: Exception) {
                _uiState.value = DetailsUiState(
                    isLoading = false,
                    errorMessage = "Não foi possível carregar os detalhes: ${e.localizedMessage}"
                )
            }
        }
    }
}

### TempoDica-main\app\src\main\java\com\example\tempodica\ui\viewmodel\WeatherViewModel.kt

package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ---- UI STATE ----
data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperature: String = "",
    val weatherDescription: String = "",
    val suggestion: String = "",
    val errorMessage: String? = null
)

// ---- UI EVENTS ----
sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    companion object {
        private const val DEFAULT_LAT = -8.69
        private const val DEFAULT_LON = -35.59
    }

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchWeather()

// Função definida na linha 49
    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = repository.getWeatherForecast(DEFAULT_LAT, DEFAULT_LON)
                val current = response.currentWeather

                val description = getWeatherDescription(current.weatherCode)
                val suggestion = getSuggestion(current.temperature, current.weatherCode)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        temperature = "${current.temperature}°C",
                        weatherDescription = description,
                        suggestion = suggestion,
                        errorMessage = null
                    )
                }
                
            } catch (e: Exception) {
                val message = "Erro ao buscar dados do clima. Tente novamente."

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                _uiEvent.emit(UiEvent.ShowToast(errorMessage))
            }
        }
    }

    // ---- DESCRIÇÕES DE CLIMA ----
// Função definida na linha 85
    private fun getWeatherDescription(code: Int): String = when (code) {
        0 -> "Céu limpo"
        1, 2, 3 -> "Parcialmente nublado"
        45, 48 -> "Nevoeiro"
        51, 53, 55 -> "Garoa"
        61, 63, 65 -> "Chuva"
        80, 81, 82 -> "Pancadas de chuva"
        95 -> "Tempestade"
        else -> "Condição desconhecida"
    }

    // ---- SUGESTÕES ----
// Função definida na linha 97
    private fun getSuggestion(temp: Double, code: Int): String {
// Retorna resultado
        return when {
            temp > 28 -> "Muito quente! Beba bastante água e evite sol forte."
            code in setOf(61, 63, 65, 80, 81, 82, 95) ->
                "Dia chuvoso. Que tal ver um filme ou série?"
            code == 0 && temp > 18 ->
                "Clima perfeito para uma caminhada ao ar livre!"
            temp < 12 ->
                "Friozinho! Um chocolate quente cai bem."
            else -> "Aproveite o dia da melhor forma!"
        }
    }
}

