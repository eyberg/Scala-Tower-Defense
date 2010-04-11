#!/bin/sh

rm -rf classes/*
scalac -d classes src/dtd.scala
