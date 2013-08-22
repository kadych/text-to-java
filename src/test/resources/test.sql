-- Получить список изданий
select t.*
  from zx.issues t
 where t.code in (1, 9) and t.isactive = 'Y';
