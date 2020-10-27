```
BFSLevels(G(V,E): graph, v: vertex from V)
 1. foreach u in V
 2.   visited[u] <- False
 3.   pred[u] <- NIL
 4.   dist[u] <- infinity
 5. Qc, Qn <- празни опашки
 6. level <- 1
 7. visited[v] <- True
 8. Qc.push(v)
 9. dist[v] <- 0
10. while Qc или Qn не е празна
11.   if Qc е празна
12.     Swap(Qc,Qn)
13.     level <- level + 1
14.   while Qc не е празна
15.     u <- Qc.pop()
16.     foreach w in Adj(u)
17.       if !visited[w]
18.         visited[w] <- True
18.         pred[w] <- u
19.         dist[w] <- level
20.         Qn.push(w)
```

Отново `T(n,m) = Θ(n+m)` и `M(n,m) = Θ(n)` със същите съображения като класическия BFS. Разликата е, че поддържаме опашка, съответстваща на текущото "ниво" (`Qc`), и опашка `Qn` за следващото ниво - т.е. всички непосетени съседи на върховете от `Qc`, текущото ниво. При изчерпване на върховете от текущото ниво се изпълнява `Swap` на ред 9, и в този момент коректно увеличаваме броя достигнати нива от `v`. Трябва да се отбележат следните няколко неща:
- тук функцията приема като аргумент начален връх и обхожда **само** достижимите от него върхове
- няма смисъл да говорим за "нива", ако не започваме от даден връх
- хубаво е (макар и незадължително за сложността) този `Swap` да се изпълнява за константно време.

След изпълнението `dist[u]` за всеки връх `u` съдържа разстоянието до него от `v` (безкрайност, ако не е достижим)