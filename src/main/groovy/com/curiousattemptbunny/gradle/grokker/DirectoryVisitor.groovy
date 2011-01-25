package com.curiousattemptbunny.gradle.grokker;

import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class DirectoryVisitor extends SimpleFileVisitor {
	def callbackClosure
	
	FileVisitResult preVisitDirectory(Object file, BasicFileAttributes attrs) throws IOException {
		callbackClosure(file)
		return FileVisitResult.CONTINUE;
	}
}
