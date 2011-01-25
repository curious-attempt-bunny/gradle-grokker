package com.curiousattemptbunny.gradle.grokker

import static java.nio.file.StandardWatchEventKind.*
import java.nio.file.WatchService
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.Path

class Grok {
	
	def static void main(args) {
		// TODO get these via a custom gradle task
		def dirsToWatch = [
			'/home/merlyn/fun/gradle-grokker/src/main/resources',
			'/home/merlyn/fun/gradle-grokker/src/main/java',
			'/home/merlyn/fun/gradle-grokker/src/main/groovy',
			'/home/merlyn/fun/gradle-grokker/src/test/resources',
			'/home/merlyn/fun/gradle-grokker/src/test/java',
			'/home/merlyn/fun/gradle-grokker/src/test/groovy'
		]
		def buildFile = '/home/merlyn/fun/gradle-grokker/build.gradle'
		
		new Grok(dirsToWatch:dirsToWatch, buildFile:buildFile).run()
	}
	
	
	def dirsToWatch
	String buildFile
	private def fileSystem
	private WatchService watchService
	
	Grok() {
		fileSystem = java.nio.file.FileSystems.getDefault()
		watchService = fileSystem.newWatchService()
	}
	
	static final def WATCH_KINDS = [ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY] as WatchEvent.Kind[]
	static final def WATCH_MODIFIERS = [] as WatchEvent.Modifier[]	
	
	def watchDirRecursive(dir) {
		watchDir(dir)
		Files.walkFileTree(dir, new DirectoryVisitor(callbackClosure: {
			watchDir(it)
		}))
	}

	def watchDir(dir) {
		println "\t$dir"
		dir.register(watchService, WATCH_KINDS, WATCH_MODIFIERS)
	}	
	
	def run() {	
		println "Watching these directories:"
		
		dirsToWatch.collect { new File(it).toPath() }.findAll { it.exists() }.each { watchDirRecursive(it) }
		
		while(true) {
			WatchKey key = watchService.take()
			def events = key.pollEvents()
			key.reset()
			
			events.each { event ->
				//println "WatchEvent = ${event.context().class} of ${event.context().class.classes} : ${event.context().class.methods.join("\n")} / ${event.count()} / ${event.kind}"
				def path = (Path)event.context()
				println "${event.kind().name()} : ${path.toAbsolutePath()}"
				//if (Files.isDirectory(path)) {
				//	watchDirRecursive(path)
				//}
			}
		}
	}

}

