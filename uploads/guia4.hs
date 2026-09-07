-- 1

fib :: Int -> Int
fib n
    | n == 0 = 0
    | n == 1 = 1
    | otherwise = fib (n - 1) + fib (n - 2)


-- 2

parteEntera :: Float -> Int 
parteEntera n 
    | n < 1 = 0
    | otherwise = 1 + parteEntera(n-1)

-- 3

esDivisible :: Int -> Int -> Bool 
esDivisible x y 
    | x == 0 = True
    | x < y = False
    | otherwise = esDivisible(x - y) y

-- 4

sumaImpares :: Int -> Int
sumaImpares n
    | n == 0 = 0
    | otherwise = (2*n -1) + sumaImpares(n - 1)

-- 5

medioFact :: Int -> Int 
medioFact n
    | n == 0 = 0
    | n == 1 = 1
    | otherwise = n * medioFact(n-2)

-- 6

todosDigitosIguales :: Int -> Bool
todosDigitosIguales n
    | n < 10 = True
    | mod n 10 == mod (div n 10) 10 = todosDigitosIguales (div n 10)
    | otherwise = False

-- 7

iesimoDigito :: Int -> Int -> Int
iesimoDigito x i = mod (div x (10^(cantDigitos x - i))) 10

cantDigitos :: Int -> Int 
cantDigitos x
    | x == 0 = 1
    | otherwise = 1 + cantDigitos(div x 10)

-- 8

sumaDigitos :: Int -> Int
sumaDigitos n 
    | n < 10 = n
    | otherwise = mod n 10 + sumaDigitos(div n 10)

-- 9

esCapicua :: Int -> Bool
esCapicua n
    | n < 10 = True
    | n > 10 && (mod n 10) /= (mod(reversaNumero n) 10) = False
    | otherwise = esCapicua (div (reversaNumero (div (reversaNumero n) 10)) 10)
    
reversaNumero :: Int -> Int 
reversaNumero n
    | n < 10 = n
    | otherwise = (mod n 10)*10^(cantDigitos n -1) + reversaNumero (div n 10)

-- 10

f1 :: Int -> Int
f1 0 = 1
f1 n = 2^n + f1(n-1)

f2 :: Int -> Float -> Float
f2 1 q = q
f2 n q = q^n + f2 (n-1) q

f3 :: Int -> Float -> Float
f3 n q = f2 (n*2) q

f4 :: Int -> Float -> Float
f4 n q = f3 n q - f2 n q + q^n

-- 11

eAprox :: Integer -> Float
eAprox x | x == 0 = 0
         | otherwise = eAprox (x - 1) + (1 / factorial2 x)

factorial2 :: Integer -> Float
factorial2 0 = 1.0
factorial2 n = fromIntegral n * factorial2 (n - 1)

e :: Float
e = eAprox 10

-- 12

raizDe2Aprox :: Int -> Float
raizDe2Aprox n = sucesion n - 1

sucesion :: Int -> Float
sucesion n | n == 1 = 2
           | otherwise = 2 + (1 / sucesion (n-1))

-- 13

sumatoriaDoble :: Int -> Int -> Int
sumatoriaDoble 1 m = sumatoriaInterna 1 m
sumatoriaDoble n m = sumatoriaInterna n m + sumatoriaDoble (n-1) m

sumatoriaInterna :: Int -> Int -> Int
sumatoriaInterna n 1 = n
sumatoriaInterna n m = n^m + sumatoriaInterna n (m-1)