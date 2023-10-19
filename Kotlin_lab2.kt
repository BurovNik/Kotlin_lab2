data class MyPoint( val x: Double, val y:Double)

class MyLine(val point1: MyPoint, val point2: MyPoint) {
    
	fun get_k(): Double{ //метод возвращает тангенс угла наклона для уравнения прямой y = kx + b 
        if (point1.x == point2.x) {
            if (point1.y == point2.y) {
                return -Double.MAX_VALUE //точки совпадают => это не прямая
            }
            else {
                return Double.MAX_VALUE //точки находятся друг над другом => вертикальная прямая
            }
        }
        if (point1.x > point2.x) { //первая точка "Правее", чем вторая
            return (point1.y - point1.y) / (point1.x - point2.x)
        }
        else { // вторая точка "Правее", чем первая
            return (point2.y - point1.y) / (point2.x - point1.x)
        }
    }
    
    fun get_b(): Double { // метод возвращает свободный член для уравнения прямой y = kx + b
        val k = this.get_k()
        
        if (k == 0.0) { //горизонтальная прямая
            return point1.y
        }
        
        if (k != Double.MAX_VALUE && k != -Double.MAX_VALUE) { // обычная прямая
            return point1.y - k * point1.x
        }
        return k // сюда попадаем только если прямая вертикальня или точки совпали
    }
    
    fun intersectionWithAxis(): List<MyPoint> {
    	val intersections = mutableListOf<MyPoint>()
        
        val k = this.get_k()
        val b = this.get_b()
        
        if (k == -Double.MAX_VALUE) {
            intersections.add(MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE))
            intersections.add(MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE))
        }
        else if (k == Double.MAX_VALUE) {
            intersections.add(MyPoint(point1.x, 0.0))
            intersections.add(MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE))
           }
        else if (k == 0.0) {
            intersections.add(MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE))
            intersections.add(MyPoint(0.0, b))
            
        }
        else {
            intersections.add(MyPoint(-b/k, 0.0))
            intersections.add(MyPoint(0.0, b))
        }
        
        return intersections
    }
}


fun lineIntersection(line1: MyLine, line2: MyLine): MyPoint {
    val k1 = line1.get_k()
    val b1 = line1.get_b()
    val k2 = line2.get_k()
    val b2 = line2.get_b()
    var x: Double
    var y: Double
    
    if (k1 == -Double.MAX_VALUE || k2 == -Double.MAX_VALUE) { //Одна из пярямых - точка (совпадают точки, задающие пярмую)
        return MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE)
    }
    else if (k1 == k2) {
        if (b1 == b2 ) {
			return MyPoint(Double.MAX_VALUE, Double.MAX_VALUE) //прямые совпадают
        }
        return MyPoint(-Double.MAX_VALUE, -Double.MAX_VALUE) //прямые параллельны
    }
    
    else {
        if (k1 == Double.MAX_VALUE) {
          	x = line1.point1.x
        	y = k2 * x + b2  
      	}  
        else if (k2 == Double.MAX_VALUE) {
          	x = line2.point1.x
        	y = k1 * x + b1  
      	}
        else {
            x = (b2 - b1) / (k1 - k2)
            y = k1 * x + b1
        }
    }
    return MyPoint(x, y)
    
}

fun isParallel(line1: MyLine, line2: MyLine): Boolean {
    val k1 = line1.get_k()
    val k2 = line2.get_k()
    
    if(k1 == -Double.MAX_VALUE || k2 == -Double.MAX_VALUE) { //считаем, что точка параллельна любой прямой
        return true
    }
    return k1 == k2
}


fun main() {

	val line1 = MyLine(MyPoint(1.0, 1.0), MyPoint(3.0, 3.0))
    val k1 = line1.get_k()
    val b1 = line1.get_b()
    val intersections = line1.intersectionWithAxis()
    
    println("k = $k1")
   	println("b = $b1") 
    println("intersections = $intersections")
   
    val line2 = MyLine(MyPoint(0.0, 4.0), MyPoint(4.0, 0.0))
    val k2 = line2.get_k()
    val b2 = line2.get_b()
    
    println("k = $k2")
   	println("b = $b2")
    val inter = lineIntersection(line1, line2)
    println("Intersection between lines  = $inter") 
    
    val lineList = mutableListOf<MyLine>(
    	MyLine(MyPoint(1.0, 1.0), MyPoint(3.0, 3.0)), // прямая y = x 
        MyLine(MyPoint(0.0, 4.0), MyPoint(4.0, 0.0)), // прямая y = -x + 4
        MyLine(MyPoint(1.0, 3.0), MyPoint(3.0, 5.0)), // прямая y = x + 2
        MyLine(MyPoint(1.0, 4.0), MyPoint(3.0, 6.0)), // прямая y = x + 3
        MyLine(MyPoint(0.0, 2.0), MyPoint(4.0, -2.0))  // прямая y = -x + 2
        )
    
    var lines = lineList
    val parallelGroups = mutableMapOf<MyLine, MutableList<MyLine>>()
	println(lines)
    
    var i: Int = 0
    while (i < lines.size){
        val line = lines[i]
        var j: Int = i + 1
        while (j < lines.size) {
        	val otherLine = lines[j] 
            if (isParallel(line, otherLine)) {
                if (!parallelGroups.containsKey(line)) {
                    parallelGroups[line] = mutableListOf()
                }
                parallelGroups[line]?.add(otherLine)
                lines.removeAt(j) //удаляем прямую, чтобы потому что она может быть только в одной группе параллельности
                j--
            }
            j++
        }
        i++
    }
    
    

    // Вывод результатов
    var count: Int = 1
     for ((line, parallelLines) in parallelGroups) {
         println("Группа параллельных прямых $count:")
         println(line)
         for (parallelLine in parallelLines) {
             println(parallelLine)
         }
         println()
         count++
     }
    
}