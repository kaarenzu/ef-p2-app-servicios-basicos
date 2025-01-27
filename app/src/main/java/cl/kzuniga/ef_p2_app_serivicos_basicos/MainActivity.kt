package cl.kzuniga.ef_p2_app_serivicos_basicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.kzuniga.ef_p2_app_serivicos_basicos.modelo.RegistroClass
import cl.kzuniga.ef_p2_app_serivicos_basicos.ui.theme.Efp2appserivicosbasicosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRegistroServicios()
        }
    }
}


@Preview(showBackground = false, showSystemUi = true)
@Composable
fun AppRegistroServicios(
    navController: NavHostController = rememberNavController(),
) {
    val listaRegistros = remember { mutableStateListOf<RegistroClass>() }
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            PageInicioUI(
                onButtonSettingsClicked = {
                    navController.navigate("agregarRegistros")
                },
                listaRegistros
            )
        }
        composable("agregarRegistros") {
            AgregarRegistrosPageUI(
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                listaRegistros
            )
        }
    }
}


//Componente header
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerNav(
    title:String = "",
    showSettingsButton:Boolean = true,
    onButtonSettingsClicked:() -> Unit = {},
    showBackButton:Boolean = false,
    onBackButtonClicked:() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if(showBackButton) {
                IconButton(onClick = {
                    onBackButtonClicked()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        },
        actions = {
            if( showSettingsButton ) {
                IconButton(onClick = {
                    onButtonSettingsClicked()
                }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Configuración"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}



// Pagina de inicio y muestra de registros
@Composable
fun PageInicioUI(
    onButtonSettingsClicked:() -> Unit = {},
    registros: MutableList<RegistroClass>
) {
    val contexto = LocalContext.current
    Scaffold(
        topBar = {
            BannerNav(
                title = contexto.getString(R.string.titulo_inicio),
                onButtonSettingsClicked = onButtonSettingsClicked
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Registros"
                )
                LazyColumn(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp))
                {
                    items(registros.size) { index: Int ->
                        RegistroItem(
                            registro = registros[index],
                            onDelete = { registros.removeAt(index) }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun RegistroItem(
    registro: RegistroClass,
    onDelete: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val tipoRegistro = registro.tipoRegistro
            val iconoTipoRegistro = when (tipoRegistro){
                "Agua" -> Icons.Filled.AddCircle
                "Luz" -> Icons.Filled.Call
                "Gas" -> Icons.Filled.Warning
                else -> Icons.Filled.Refresh
            }
            Icon(
                imageVector = iconoTipoRegistro,
                contentDescription = "IconoTipoRegistro",
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.Cyan
            )
            Text(text = registro.tipoRegistro)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = registro.registro)
            Text(text = "Fecha:  ${registro.fecha}")

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "EliminarRegistro",
                    tint = Color.Red,
                )
            }
        }
        HorizontalDivider()
    }
}


//Pagina de registros
//@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AgregarRegistrosPageUI(
    onBackButtonClicked:() -> Unit = {},
    registros: MutableList<RegistroClass>
) {
    val contexto = LocalContext.current

    var registroMedidorValue by remember { mutableStateOf("") }
    var registroFechaValue by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Agua") }

    Scaffold(
        topBar = {
            BannerNav(
                title = contexto.getString(R.string.titulo_agregar),
                showSettingsButton = false,
                showBackButton = true,
                onBackButtonClicked = onBackButtonClicked
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = paddingValues.calculateTopPadding())
            ){
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = registroMedidorValue,
                    placeholder = { Text(text = "ingrese registro") },
                    onValueChange = { registroMedidorValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = registroFechaValue,
                    placeholder = { Text(text = "ingrese fecha") },
                    onValueChange = { registroFechaValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text =  contexto.getString(R.string.form_tipo_medidor),
                )
                Spacer(modifier = Modifier.height(16.dp))
                //Opcion Agua
                Row {
                    RadioButton(
                        selected = selectedOption == "Agua",
                        onClick = { selectedOption = "Agua" }
                    )
                    Text(text = contexto.getString(R.string.form_select_agua))
                }
                //Opcion Luz
                Row {
                    RadioButton(
                        selected = selectedOption == "Luz",
                        onClick = { selectedOption = "Luz" }
                    )
                    Text(text =  contexto.getString(R.string.form_select_luz))
                }
                //Opcion Gas
                Row {
                    RadioButton(
                        selected = selectedOption == "Gas",
                        onClick = { selectedOption = "Gas" }
                    )
                    Text(text =  contexto.getString(R.string.form_select_gas))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val registro = RegistroClass(
                            registro = registroMedidorValue,
                            fecha = registroFechaValue,
                            tipoRegistro = selectedOption
                        )

                        registros.add(registro)
                    }
                ) {
                    Text(contexto.getString(R.string.btn_agregar_registro))
                }
            }
        }
    )
}