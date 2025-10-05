workspace "Price Tracker" "An Android application for tracking personal grocery prices locally"{

    !identifiers hierarchical

    model {
        # --- Definición de personas/actores ---
        u = person "Comprador" "Usuario que registra los precios de sus compras en la app móvil" "Buyer"
        # --- Definición de Sistemas ---
        ss = softwareSystem "Price Tracker App" {
            aa = container "App Android" "Ofrece la interfaz para introducir y visualizar precios de compras" "Android (Kotlin)" "AndroidApp"
            db = container "Base de datos" {
                description "Almacena información de las compras"
                technology "Room DB"
                tags "Database"
            }
        }
        # --- Definición de las relaciones ---
        u -> ss "Usa"
        u -> ss.aa "Usa"
        ss.aa -> ss.db "Lee de y escribe en"
    }

    views {
        systemContext ss "ContextDiagram" "Diagrama de Contexto C4 para la app Price Tracker"  {
            include *
            autolayout lr
        }

        container ss "ContainerDiagram" {
            include *
            autolayout lr
        }

        styles {
            element "Person" {
                background #08427b
                color #ffffff
                shape Person
            }
            element "Software System" {
                background #3ddc84
                color #ffffff
            }
            element "AndroidApp" {
                background #008242
                color #ffffff
                shape Roundedbox
            }
             element "Database" {
                background #008242
                color #ffffff
                shape Cylinder
            }
        }
    }
}