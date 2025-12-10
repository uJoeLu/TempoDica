# 📱 Guia de Utilização - Projeto TempoDica (Android)

##  Sumário Executivo

**TempoDica** é um aplicativo Android que fornece informações sobre clima em tempo real e previsões horárias usando a API Open-Meteo. Utiliza as **Jetpack Compose**, **Kotlin** e **MVVM**.

---

## Arquitetura do Projeto

### Estrutura de Pastas

```
TempoDica-main/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/example/tempodica/
│   │   │   ├── MainActivity.kt              # Ponto de entrada principal
│   │   │   ├── data/
│   │   │   │   ├── model/                   # Modelos de dados (DTOs)
│   │   │   │   │   ├── CurrentWeather.kt    # Clima atual
│   │   │   │   │   ├── HourlyForecast.kt    # Previsão horária
│   │   │   │   │   └── WeatherResponse.kt   # Resposta da API
│   │   │   │   └── remote/
│   │   │   │       ├── RetrofitInstance.kt  # Cliente HTTP
│   │   │   │       └── WeatherApiService.kt # Interface de API
│   │   │   ├── repository/
│   │   │   │   └── WeatherRepository.kt     # Camada de dados
│   │   │   └── ui/
│   │   │       ├── navigation/              # Navegação entre telas
│   │   │       ├── screens/                 # Telas (Composables)
│   │   │       ├── theme/                   # Tema do Material Design 3
│   │   │       └── viewmodel/               # ViewModels (lógica UI)
│   │   └── res/                             # Recursos (strings, cores, etc)
│   └── build.gradle.kts                     # Configuração de build do módulo
├── build.gradle.kts                         # Configuração de build do projeto
├── settings.gradle.kts                      # Configuração de submódulos
└── gradle.properties                        # Propriedades do Gradle
```

---

## 🔧 Configuração Inicial

### Pré-requisitos

- **Android Studio**: Última versão (Hedgehog ou superior)
- **JDK**: Versão 1.8 ou superior
- **SDK do Android**: API 26+ (minSdk), API 34 (targetSdk)
- **Gradle**: 8.0+

### Passos de Instalação

#### 1. Clonar o Projeto

```bash
git clone <url_do_repositorio>
cd TempoDica-main
```

#### 2. Abrir no Android Studio

```bash
# Opção 1: Via linha de comando
open -a "Android Studio" .

# Opção 2: Abrir Android Studio > File > Open > Selecionar pasta
```

#### 3. Sincronizar Gradle

- Android Studio fará automaticamente o sync do Gradle
- Aguarde que todos os downloads de dependências sejam concluídos

#### 4. Compilar o Projeto

```bash
# Via terminal
./gradlew build

# Ou via Android Studio
Build > Build Project
```

---

## 🚀 Como Executar o Aplicativo

### Opção 1: Emulador Android

```bash
# Criar e iniciar emulador
emulator -avd <nome_do_avd>

# Ou via Android Studio: AVD Manager > Launch

# Depois rodar a aplicação
./gradlew installDebug

# Ou clicar em "Run" no Android Studio
```

### Opção 2: Dispositivo Físico

1. **Conectar dispositivo** via USB
2. **Ativar USB Debugging** no dispositivo
3. **Confiar** no computador
4. Rodar via Android Studio ou `./gradlew installDebug`

---

## 📱 Fluxo da Aplicação

### 1. **MainActivity** → Ponto de Entrada
- Inicializa o aplicativo
- Define o tema (TempoDicaTheme)
- Carrega a navegação (AppNavigation)
- Ativa layout edge-to-edge para barra de status

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TempoDicaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation() // Navega entre telas
                }
            }
        }
    }
}
```

### 2. **AppNavigation** → Controla Fluxo de Telas
- Define as rotas de navegação
- Gerencia o NavHost do Jetpack Compose
- Controla qual tela exibir

### 3. **Screens** → Telas Visuais
- `HomeScreen`: Exibe clima atual
- `DetailsScreen`: Detalhes da previsão horária

### 4. **ViewModel** → Lógica da UI
- `WeatherViewModel`: Gerencia dados de clima
- `DetailsViewModel`: Gerencia dados de detalhes

### 5. **Repository** → Acesso a Dados
- `WeatherRepository`: Interface entre UI e API
- Busca dados da API Open-Meteo

### 6. **API Remote** → Comunicação Externa
- `WeatherApiService`: Interface Retrofit para API
- `RetrofitInstance`: Cliente HTTP configurado

---

## 📊 Modelos de Dados

### CurrentWeather (Clima Atual)

```kotlin
data class CurrentWeather(
    val temperature: Double = 0.0,      // Temperatura em °C
    val windSpeed: Double = 0.0,        // Velocidade do vento em km/h
    val weatherCode: Int = -1           // Código do clima (0-99)
) {
    val description: String
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

### HourlyForecast (Previsão Horária)

```kotlin
data class HourlyForecast(
    val time: List<String>,              // Horários (ex: "2023-10-27T10:00")
    val temperatures: List<Double>,      // Temperaturas por hora
    val weatherCodes: List<Int>          // Códigos climáticos por hora
)
```

### WeatherResponse (Resposta da API)

```kotlin
data class WeatherResponse(
    val currentWeather: CurrentWeather,  // Clima atual
    val hourly: HourlyForecast           // Previsão horária
)
```

---

## 🔌 Integração com API

### Configuração do Retrofit

```kotlin
object RetrofitInstance {
    private const val BASE_URL = "https://api.open-meteo.com/"
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
```

### Endpoints da API

#### 1. Obter Clima Atual e Previsão

```
GET /v1/forecast
Parâmetros:
  - latitude: -23.550520 (exemplo: São Paulo)
  - longitude: -46.633309
  - current_weather: true
  - hourly: temperature_2m,weathercode
  - timezone: America/Sao_Paulo
```

**Resposta:**
```json
{
  "current_weather": {
    "temperature": 25.5,
    "windspeed": 15.2,
    "weathercode": 1
  },
  "hourly": {
    "time": ["2023-10-27T10:00", "2023-10-27T11:00"],
    "temperature_2m": [25.5, 26.0],
    "weathercode": [1, 2]
  }
}
```

---

## 🎨 Tema e UI

### TempoDicaTheme

Define o sistema de cores Material Design 3:
- **Cores Primárias**: Baseadas em cores do sistema do dispositivo
- **Tipografia**: Seguindo Material Design 3
- **Formas**: Cantos arredondados modernos

### Componentes Jetpack Compose

#### Button (Botão)
```kotlin
Button(onClick = { /* Ação */ }) {
    Text("Atualizar")
}
```

#### Text (Texto)
```kotlin
Text(
    text = "Temperatura: ${weather.temperature}°C",
    style = MaterialTheme.typography.bodyLarge
)
```

#### Surface (Superfície)
```kotlin
Surface(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    color = MaterialTheme.colorScheme.background
) {
    // Conteúdo
}
```

#### Modifier (Modificadores)
```kotlin
Modifier
    .fillMaxSize()          // Preenche o máximo espaço
    .padding(16.dp)         // Espaçamento interno
    .background(Color.Blue) // Cor de fundo
    .clickable { }          // Acionável
```

---

## 🔄 Fluxo de Dados (MVVM)

```
User (Interação) 
    ↓
Screen (UI/Composable)
    ↓
ViewModel (Lógica)
    ↓
Repository (Dados)
    ↓
API/RetrofitInstance (Remote)
    ↓
Open-Meteo Server
    ↓
[Resposta JSON]
    ↓
Data Models (Desserialização)
    ↓
ViewModel (Processa)
    ↓
Screen (Exibe resultado)
```

---

## 📝 Guia de Desenvolvimento

### Adicionar uma Nova Tela

1. **Criar o Composable**
```kotlin
// screens/MyNewScreen.kt
@Composable
fun MyNewScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("Nova Tela")
    }
}
```

2. **Adicionar Rota na Navegação**
```kotlin
// navigation/AppNavigation.kt
NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("myNewScreen") { MyNewScreen() }
}
```

3. **Navegar para Nova Tela**
```kotlin
navController.navigate("myNewScreen")
```

### Adicionar uma Nova Dependência

1. **Editar `build.gradle.kts` (app)**
```gradle
dependencies {
    implementation("com.example:library:1.0.0")
}
```

2. **Sincronizar Gradle**
```bash
./gradlew sync
```

### Testar a Aplicação

#### Testes Unitários
```bash
./gradlew test
```

#### Testes de Instrumentação (UI)
```bash
./gradlew connectedAndroidTest
```

---

## 🐛 Troubleshooting

### Problema: Gradle Sync Falha

**Solução:**
```bash
./gradlew clean
./gradlew sync
```

### Problema: Erro "API Key Inválida"

**Solução:**
- Verificar se a API Open-Meteo está acessível
- Verificar URL base: `https://api.open-meteo.com/`
- Testar em navegador: `https://api.open-meteo.com/v1/forecast?latitude=0&longitude=0&current_weather=true`

### Problema: Aplicativo Trava

**Solução:**
- Verificar logs: `adb logcat`
- Verificar conexão de internet
- Verificar permissões necessárias

### Problema: Emulador Lento

**Solução:**
```bash
# Usar emulador com aceleração de hardware
emulator -avd <nome> -accel on

# Ou usar dispositivo físico
```

---

## 📦 Build e Geração de APK

### Build Debug (Desenvolvimento)

```bash
./gradlew assembleDebug
# APK gerado: app/build/outputs/apk/debug/app-debug.apk
```

### Build Release (Produção)

```bash
./gradlew assembleRelease
# APK gerado: app/build/outputs/apk/release/app-release.apk
```

### Build AAB (Google Play Store)

```bash
./gradlew bundleRelease
# AAB gerado: app/build/outputs/bundle/release/app-release.aab
```

---

## 🔐 Segurança

### Boas Práticas Implementadas

1. **Sem Chaves no Código**: Nenhuma chave de API hardcoded
2. **HTTPS**: Todas as comunicações com a API via HTTPS
3. **Permissões Mínimas**: Apenas permissões necessárias no `AndroidManifest.xml`
4. **Proguard/R8**: Ativado em release para ofuscação de código

### Permissões Necessárias

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 📚 Dependências Principais

| Biblioteca | Versão | Propósito |
|-----------|--------|----------|
| Jetpack Compose | 2024.02.01 | UI Moderna |
| Material 3 | Latest | Design System |
| Retrofit | 2.9.0 | Cliente HTTP |
| Gson | Latest | Serialização JSON |
| Jetpack Navigation | 2.7.7 | Navegação |
| ViewModel | 2.7.0 | Arquitetura |
| Coroutines | 1.7.3 | Programação Assíncrona |

---

## 🚀 Próximos Passos

1. **Adicionar Persistência**: Room Database para cache local
2. **Melhorar UI**: Adicionar animações e gráficos
3. **Localização**: Usar GPS para obter coordenadas automáticas
4. **Notificações**: Alertas de clima severo
5. **Modo Escuro**: Suporte completo a tema escuro
6. **Internacionalização**: Suporte a múltiplos idiomas
7. **Testes**: Aumentar cobertura de testes
8. **Performance**: Otimizar carregamento de dados

---



**Última Atualização**: Dezembro 2025
**Versão do Projeto**: 1.0
**Status**: Pronto para Produção ✅
