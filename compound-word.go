package main

import (
	"bufio"
	"fmt"
	"os"
)


var wordMap = make(map[string]bool)

func main() {

	if len(os.Args) != 2 {
		fmt.Println("usage: compound-word word_file_name")
		os.Exit(1)
	}

	buildMap(os.Args[1])

	longestWord := ""
	for key, _ := range wordMap {
		var partSlice = getElementList(key)
		if partSlice == nil {
			continue
		}
		
		if len(key) > len(longestWord) {
			longestWord = key
		}
	}
	fmt.Println(longestWord, getElementList(longestWord))
}

func buildMap(wordFileName string) {
	
	inFile, err := os.Open(wordFileName)
	defer inFile.Close()
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}

	scanner := bufio.NewScanner(inFile)
	scanner.Split(bufio.ScanLines)

	for scanner.Scan() {
		wordMap[scanner.Text()] = true
	}
}


func getElementList(word string) []string {

	if (len(word) < 2){
		return nil
	}

	for i := 1; i < len(word); i++ {
		start := word[:i]
		end := word[i:]

		_, startExists := wordMap[start]

		if !startExists {
			continue
		}
		
		_, endExists := wordMap[end]

		if endExists {
			returnList := make([]string, 2)
			returnList[0] = start
			returnList[1] = end
			return returnList
		}

		// recursive call 
		var tailList = getElementList(end)
		if tailList == nil {
			continue
		}

		returnList := make([]string, 1, len(tailList) + 1)
		returnList[0] = start
		returnList = append(returnList, tailList...)
		return returnList

	}
	return nil
}
