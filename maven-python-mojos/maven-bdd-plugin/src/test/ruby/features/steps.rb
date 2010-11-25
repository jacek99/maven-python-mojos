class Calculator
   def push (n)
       @args ||=[]
       @args << n
   end
 end
 
Given /I have entered (.*) into the calculator/ do |n|
     calculator = Calculator.new
     calculator.push(n.to_i)
end