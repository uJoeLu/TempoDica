# 📱 Guia de Utilização - Projeto TempoDica (Android)

##  Sumário Executivo

**TempoDica** é um aplicativo Android que fornece informações sobre clima em tempo real e previsões horárias usando a API Open-Meteo. Utiliza as melhores práticas modernas de desenvolvimento Android com **Jetpack Compose**, **Kotlin** e **MVVM**.

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