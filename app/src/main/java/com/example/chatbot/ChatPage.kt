package com.example.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.ui.theme.ModelMessage
import com.example.chatbot.ui.theme.Purple80
import com.example.chatbot.ui.theme.UserMessage

@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput(onSend = {
            viewModel.sendMessage(it)
        })
    }


}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageModel>
) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Question Answer",
                tint = Purple80
            )
            Text(text = "Ask me anything", fontSize = 24.sp)
        }
    } else {

        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                MessageItem(messageModel = it)
            }
        }
    }

}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    messageModel: MessageModel,
) {
    val isModel = messageModel.role == "model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ModelMessage else UserMessage)
                    .padding(16.dp)
            ) {
                SelectionContainer{
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (text, setText) = remember { mutableStateOf("") }
        OutlinedTextField(
            value = text, onValueChange = setText,
            label = { Text("Type a message") },
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            if (text.isNotEmpty()) {
                onSend(text)
                setText("")
            }
        }) {
            Icon(Icons.Filled.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = "ChatBot",
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}