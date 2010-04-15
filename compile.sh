#!/bin/sh

CLASSPATH=lib/*:.

rm -rf classes/*
scalac -cp $CLASSPATH -d classes src/dtd.scala
