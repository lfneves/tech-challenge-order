insert into public.tb_order (
    id,
    external_id,
    id_client,
    is_finished,
    status,
    total_price,
    waiting_time)
values (
    1,
    'cdfe4e61-ec36-4a7c-933e-d49d75af963e'::UUID,
    1,
    false,
    'Pendente',
    19.99,
    now())
;

insert into public.tb_order_product (
    id_order,
    id_product)
values (
    1,
    1)
;