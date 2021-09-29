package com.erisu.cloud.megumi.util

import cn.hutool.core.lang.UUID
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.mamoe.mirai.message.data.AudioCodec
import java.io.File


object VoiceUtil {

    fun convertToAmr(inputFileName: String) {
        val ffpmeg =
            FFmpeg("D:\\ffmpeg-4.3.1-2021-01-01-full_build\\ffmpeg-4.3.1-2021-01-01-full_build\\bin\\ffmpeg.exe")
        val ffprobe =
            FFprobe("D:\\ffmpeg-4.3.1-2021-01-01-full_build\\ffmpeg-4.3.1-2021-01-01-full_build\\bin\\ffprobe.exe")

        val builder = FFmpegBuilder()
            .setInput(inputFileName) // Filename, or a FFmpegProbeResult
            .overrideOutputFiles(true) // Override the output if it exists
            .addOutput("${FileUtil.localCachePath}${File.separator}output.pcm") // Filename for the destination
            .setAudioSampleRate(24000)
            .setFormat("s16le")
            .setAudioChannels(1)
            .done()
        val executor = FFmpegExecutor(ffpmeg, ffprobe)
        executor.createJob(builder).run()
    }
}