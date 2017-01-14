package tsf_parser.app;
public class TSFMember{
  private String name;
  private int[] answers = {0,0,0};
  private int selectedMonth;
  
  TSFMember(String name, int selectedMonth, int month0, int month1, int month2){
    this.name = name;
    this.selectedMonth = selectedMonth;
    this.answers[0] = month0;
    this.answers[1] = month1;
    this.answers[2] = month2;
  }
  
  TSFMember(String name, int selectedMonth){
    this(name, selectedMonth, 0, 0, 0);
  }
  
  public String getName(){
    return this.name;
  }
  
  public int getTotal(){
   return (this.answers[0] + this.answers[1] + this.answers[2]); 
  }
  
  public void setMonth0(int value){
    this.answers[0] = value;
  }
  
  public void setMonth1(int value){
    this.answers[0] = value;
  }
    
  public void setMonth2(int value){
    this.answers[0] = value;
  }
  
  public int getMonth(int month){
    if (month <= 2 && month >= 0){
      return this.answers[month];
    }
    
    return 0;
  }
  
  public void setMonth(int month, int value){
    if (month <= 2 && month >= 0){
      this.answers[month] = value;
    }
  }
  
  public String getSQLStatement(String month_name){
//    return "UPDATE stats SET " + month_name + "='" + this.answers[1] + "' WHERE name='" + this.name + "';";
    return String.format("UPDATE stats SET %s='%s' WHERE name='%s';", month_name, this.answers[this.selectedMonth], this.name);
  }
  
  public String getSQLInsertStatement(){
//    return "INSERT INTO stats (name) VALUES ('" + this.name + "');";
    return String.format("INSERT INTO stats (name) VALUES ('%s');", this.name);
  }
  
}