package com.alaazarifa.linencryptotest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alaazarifa.linencryptotest.viewmodel.TokenBalanceViewModel
import com.alaazarifa.linencryptotest.ui.theme.LinenCryptoTestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinenCryptoTestTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                    GetEtherBalance()
                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                    GetTokenBalance()
                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                    // GetTokenTransfers()
                }
            }
        }
    }

    /*
    In this function I used web3j to get the current ETH balance for a specific address.
    */
    @Composable
    fun GetEtherBalance() {
        var totalBalance: BigDecimal? by remember { mutableStateOf(null) }
        LaunchedEffect(Unit) {
            val web3j = Web3j.build(HttpService("https://rpc.ankr.com/eth"))
            val address = "0x7DBB4bdCfE614398D1a68ecc219F15280d0959E0"
            launch(Dispatchers.IO) {
                val ethBalance =
                    web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync()
                        .get(10, TimeUnit.SECONDS)
                val etherBalance =
                    Convert.fromWei(ethBalance.balance.toString(), Convert.Unit.ETHER)
                totalBalance = etherBalance
            }
        }
        Text(text = if (totalBalance == null) "Getting ETH balance..." else "ETH Balance = $totalBalance")
    }


    /*
    I couldn't find a straight-forward solution to get the current token balance of ERC-20 token using web3j or kethereum
    but after some searching, I found a way to get the balance using the [etherscan API] and it works
    */
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun GetTokenBalance() {
        val viewModel = remember { TokenBalanceViewModel() }
        val totalBalance by viewModel.balance.collectAsState()
        LaunchedEffect(Unit) { viewModel.fetchTokenBalance() }
        Box {
            Text(text = if (totalBalance == null) "Getting current token balance..." else "Token Balance = $totalBalance")
        }
    }

    /*
     I couldn't find a solution to get the incoming token transfers using web3j or kethereum
     I'm sure there's a way, but since I have no experience dealing with these libraries I couldn't figure a way to do it.
     */
    @Composable
    fun GetTokenTransfers() {

    }
}



