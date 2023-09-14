package com.kotlinconf.workshop.channelsdemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
   val channel = Channel<Int>(10)

   // Coroutine to send values to the channel
   launch {
      for (i in 1..8) {
         println("Sending $i...")
         channel.send(i)
         println("Sent $i.")
         delay(300)
      }
      channel.close() // Close the channel when done sending
   }

   // Slow Consumer
   launch {
      for (value in channel) {
         println("A received $value")
      }
   }

   // Multiple consumers
   launch {
      for (value in channel) {
         println("B received $value")
      }
   }
}

// Output (example)::
//
// Sending 1...
// Sent 1.
// A received 1
// Sending 2...
// Sent 2.
// A received 2
// Sending 3...
// Sent 3.
// B received 3
// Sending 4...
// Sent 4.
// A received 4
// Sending 5...
// Sent 5.
// B received 5
// Sending 6...
// Sent 6.
// A received 6
// Sending 7...
// Sent 7.
// B received 7
// Sending 8...
// Sent 8.
// A received 8
